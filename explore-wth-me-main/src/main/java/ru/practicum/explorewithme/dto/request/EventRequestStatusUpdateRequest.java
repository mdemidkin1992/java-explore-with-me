package ru.practicum.explorewithme.dto.request;

import lombok.Data;
import ru.practicum.explorewithme.model.enums.RequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private RequestStatus status;
}
