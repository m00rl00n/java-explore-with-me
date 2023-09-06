package ru.practicum.request.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongDataException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestDtoMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceImpl implements RequestService {

    final EventRepository eventRepository;
    final RequestDtoMapper requestDtoMapper;
    final UserService userService;
    final RequestRepository requestRepository;

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsDto(Long userId, Long eventId) {
        log.info("Получение информации...... ");
        List<ParticipationRequest> requests = getParticipationRequests(userId, eventId);
        return mapRequestsToDto(requests);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult update(Long userId,
                                                 Long eventId,
                                                 EventRequestStatusUpdateRequest updateRequest) {
        log.info("Изменение статуса заявок на участие в событии пользователя " + userId);
        Event event = getEventById(eventId);
        List<ParticipationRequest> requests = getParticipationRequestsByEventId(eventId);
        Long confirmedRequestsCounter = 0L;
        List<ParticipationRequest> result = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (ParticipationRequest request : requests) {
            if (isValidRequestStatus(request, updateRequest.getStatus(), event)) {
                updateRequestStatus(request, updateRequest.getStatus());
                ParticipationRequestDto participationRequestDto = requestDtoMapper.mapRequestToDto(request);
                if ("CONFIRMED".equals(participationRequestDto.getStatus())) {
                    confirmedRequests.add(participationRequestDto);
                } else if ("REJECTED".equals(participationRequestDto.getStatus())) {
                    rejectedRequests.add(participationRequestDto);
                }
                result.add(request);
                confirmedRequestsCounter++;
            } else {
                throw new WrongDataException("Неверный статус запроса");
            }
        }

        handleExcessiveRequests(event, confirmedRequestsCounter);
        requestRepository.saveAll(result);

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
        eventRequestStatusUpdateResult.setConfirmedRequests(confirmedRequests);
        eventRequestStatusUpdateResult.setRejectedRequests(rejectedRequests);
        return eventRequestStatusUpdateResult;
    }

    @Override
    public List<ParticipationRequestDto> getParticipationRequestsByUserId(Long userId) {
        log.info("Получение информации о заявках пользователя...");
        List<ParticipationRequest> requests = requestRepository.findByUserId(userId);
        return mapRequestsToDto(requests);
    }

    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        log.info("Заявка пользователем " + userId + " запроса на участие в событии " + eventId);
        User user = userService.getUserById(userId);
        Event event = getEventById(eventId);

        validateRequest(user, event);

        ParticipationRequest newRequest = createParticipationRequest(user, event);
        return requestDtoMapper.mapRequestToDto(requestRepository.save(newRequest));
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        log.info("Отмена запроса на участие ");
        User user = userService.getUserById(userId);
        ParticipationRequest request = getRequestById(requestId);
        validateRequestOwnership(request, userId);
        request.setStatus("CANCELED");
        log.info("Отмена заявки на участие " + requestId);
        return requestDtoMapper.mapRequestToDto(requestRepository.save(request));
    }

    private List<ParticipationRequestDto> mapRequestsToDto(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(requestDtoMapper::mapRequestToDto)
                .collect(Collectors.toList());
    }

    private void updateRequestStatus(ParticipationRequest request, String status) {
        if ("REJECTED".equals(request.getStatus()) && "CONFIRMED".equals(status)) {
            throw new ConflictException("Нельзя отменять подтверждённую заявку");
        }
        request.setStatus(status);
    }

    private boolean isValidRequestStatus(ParticipationRequest request, String newStatus, Event event) {
        if ("CONFIRMED".equals(newStatus) && event.getParticipantLimit() != 0) {
            if (event.getParticipantLimit() <= countConfirmedRequests(event)) {
                rejectExcessivePendingRequests(event);
                throw new ConflictException("Слишком много заявок на участие");
            }
        }
        return Arrays.asList("CONFIRMED", "REJECTED", "PENDING").contains(request.getStatus());
    }

    private void rejectExcessivePendingRequests(Event event) {
        List<ParticipationRequest> pending = requestRepository.findRequestByEventIdAndStatus(event.getId(), "PENDING");
        for (ParticipationRequest p : pending) {
            p.setStatus("REJECTED");
        }
        requestRepository.saveAll(pending);
    }

    private void handleExcessiveRequests(Event event, Long confirmedRequestsCounter) {
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() < confirmedRequestsCounter) {
            rejectExcessivePendingRequests(event);
            throw new ConflictException("Слишком много заявок на участие");
        }
    }

    private Long countConfirmedRequests(Event event) {
        return requestRepository.countByEventAndStatuses(event.getId(), List.of("CONFIRMED", "ACCEPTED"));
    }

    private void validateRequest(User user, Event event) {
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new ConflictException("Владелец не может подать заявку на участие в своём событии");
        }
        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new ConflictException("Событие не опубликовано, заявка не подана");
        }
        if (participationLimitIsFull(event)) {
            throw new ConflictException("Достигнут лимит заявок, заявка не подана");
        }
        if (hasDuplicateRequest(user.getId(), event.getId())) {
            throw new ConflictException("Оставить заявку повторно невозможно");
        }
    }

    private boolean participationLimitIsFull(Event event) {
        Long confirmedRequestsCounter = countConfirmedRequests(event);
        return event.getParticipantLimit() != 0 && event.getParticipantLimit() <= confirmedRequestsCounter;
    }

    private boolean hasDuplicateRequest(Long userId, Long eventId) {
        return getParticipationRequestsByEventId(eventId).stream()
                .anyMatch(request -> request.getRequester().getId().equals(userId));
    }

    private ParticipationRequest createParticipationRequest(User user, Event event) {
        ParticipationRequest newRequest = new ParticipationRequest();
        newRequest.setRequester(user);
        newRequest.setCreated(LocalDateTime.now().toString());
        newRequest.setStatus(event.getParticipantLimit() == 0 ? "CONFIRMED" : "PENDING");
        newRequest.setEvent(event);
        if (!event.getRequestModeration()) {
            newRequest.setStatus("ACCEPTED");
        }
        return newRequest;
    }

    private void validateRequestOwnership(ParticipationRequest request, Long userId) {
        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("Заявка оставлена другим пользователем");
        }
    }

    private ParticipationRequest getRequestById(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос не существует")
        );
    }

    private List<ParticipationRequest> getParticipationRequests(Long userId, Long eventId) {
        User user = userService.getUserById(userId);
        Event event = getEventById(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new WrongDataException("Пользователь не создавал событие");
        }
        return requestRepository.findByEventInitiatorId(userId);
    }

    private List<ParticipationRequest> getParticipationRequestsByEventId(Long eventId) {
        Event event = getEventById(eventId);
        return requestRepository.findByEventId(eventId);
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Событие не найдено"));
    }
}