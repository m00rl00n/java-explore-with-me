package ru.practicum.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.request.model.ParticipationRequest;

@UtilityClass
public class RequestDtoMapper {

    public ParticipationRequestDto mapRequestToDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(participationRequest.getId());
        dto.setRequester(participationRequest.getRequester().getId());
        dto.setEvent(participationRequest.getEvent().getId());
        dto.setStatus(participationRequest.getStatus());
        dto.setCreated(participationRequest.getCreated());
        return dto;
    }
}
