package ru.practicum.event.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEventController {

    final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEventByUser(@PathVariable Long userId,
                                          @Valid @RequestBody NewEventDto newEvent) {
        return eventService.addEvent(userId, newEvent);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventOfUserByIds(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        return eventService.getEventOfUserByIds(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventOfUserByIds(@PathVariable Long userId,
                                               @PathVariable Long eventId,
                                               @Valid @RequestBody UpdateEventUserRequest request) {
        return eventService.updateEventOfUserByIds(userId, eventId, request);
    }
}
