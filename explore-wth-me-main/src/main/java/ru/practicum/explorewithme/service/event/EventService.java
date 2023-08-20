package ru.practicum.explorewithme.service.event;

import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.model.enums.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEventsByUser(long userId, int from, int size);

    EventFullDto addNewEvent(long userId, NewEventDto newEventDto);

    EventFullDto getEventFullInfo(long userId, long eventId);

    EventFullDto updateEventRequest(long eventId, UpdateEventUserRequest request);

    List<ParticipationRequestDto> getParticipationRequests(long userId, long eventId);

    List<EventFullDto> getEventsByAdmin(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            int from,
            int size
    );

    EventRequestStatusUpdateResult updateStatusesOfRequestsForEvent(
            long userId,
            long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    );

    List<EventShortDto> getEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size,
            String app,
            HttpServletRequest request
    );

    EventFullDto getEvent(long eventId, String app, HttpServletRequest request);
}
