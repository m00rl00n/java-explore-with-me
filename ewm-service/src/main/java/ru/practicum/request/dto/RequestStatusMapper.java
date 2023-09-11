package ru.practicum.request.dto;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.List;

@UtilityClass
public class RequestStatusMapper {

    public static RequestStatusUpdateResult mapToEventRequestStatusUpdateResult(List<ParticipationRequestDto> confirmedRequests,
                                                                                List<ParticipationRequestDto> rejectedRequests) {
        RequestStatusUpdateResult result = new RequestStatusUpdateResult();
        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }
}