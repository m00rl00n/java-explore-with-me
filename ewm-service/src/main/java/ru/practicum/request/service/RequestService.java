package ru.practicum.request.service;

import ru.practicum.request.dto.RequestStatusUpdate;
import ru.practicum.request.dto.RequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {


    List<ParticipationRequestDto> getParticipationInvite(Long userId, Long eventId);

    RequestStatusUpdateResult update(Long userId,
                                     Long eventId,
                                     RequestStatusUpdate updateRequest);

    List<ParticipationRequestDto> getRequestsByUserId(Long userId);

    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

}
