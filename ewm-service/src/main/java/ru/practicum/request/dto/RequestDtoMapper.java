package ru.practicum.request.dto;

import org.springframework.stereotype.Component;
import ru.practicum.request.model.ParticipationRequest;

@Component
public class RequestDtoMapper {
    public ParticipationRequestDto mapRequestToDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .requester(participationRequest.getRequester().getId())
                .event(participationRequest.getEvent().getId())
                .status(participationRequest.getStatus())
                .created(participationRequest.getCreated())
                .build();
    }
}
