package ru.practicum.event.service;

import ru.practicum.event.dto.*;

import java.util.List;

public interface EventService {
    List<EventFullDto> get(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateRequest);

    List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto add(Long userId, NewEventDto newEvent);

    EventFullDto getEventDtoById(Long eventId, String uri, String ip);

    EventFullDto getEventOfUserByIds(Long userId, Long eventId);

    EventFullDto updateEventOfUserByIds(Long userId, Long eventId, UpdateEventUserRequest request);

    List<EventShortDto> getEventsWithFilters(String text,
                                             List<Long> categories,
                                             Boolean paid,
                                             String rangeStart,
                                             String rangeEnd,
                                             Boolean onlyAvailable,
                                             String sort,
                                             Integer from,
                                             Integer size,
                                             String uri,
                                             String ip);
}
