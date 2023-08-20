package ru.practicum.explorewithme.service.request;

import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    List<ParticipationRequestDto> getRequestsByUserId(long userId);

    ParticipationRequestDto addNewParticipationRequest(long userId, long eventId);

    ParticipationRequestDto cancelParticipationRequest(long userId, long requestId);
}
