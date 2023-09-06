package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

@AllArgsConstructor
@Component
public class EventRequestStatusUpdateResultMapper {

    public static EventRequestStatusUpdateResult mapToEventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests,
                                                                                     List<ParticipationRequestDto> rejectedRequests) {
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }
}