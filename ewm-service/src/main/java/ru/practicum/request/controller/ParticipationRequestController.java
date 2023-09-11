package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.RequestStatusUpdate;
import ru.practicum.request.dto.RequestStatusUpdateResult;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events/{eventId}/requests")
public class ParticipationRequestController {

    final RequestService requestService;

    @PatchMapping
    public RequestStatusUpdateResult update(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @RequestBody RequestStatusUpdate request) {
        return requestService.update(userId, eventId, request);
    }

    @GetMapping
    public List<ParticipationRequestDto> getParticipationInvite(@PathVariable Long userId,
                                                                @PathVariable Long eventId) {
        return requestService.getParticipationInvite(userId, eventId);
    }
}






