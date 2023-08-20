package ru.practicum.explorewithme.controller.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.event.EventService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class UsersEventsController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addNewEvent(
            @PathVariable long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        log.info("POST request for new Event received: {}", newEventDto);
        EventFullDto response = eventService.addNewEvent(userId, newEventDto);
        log.info("New event added: {}", response);
        return response;
    }

    @GetMapping
    public List<EventShortDto> getEventsByUser(
            @PathVariable long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        log.info("GET request for Events from user {}", userId);
        List<EventShortDto> response = eventService.getEventsByUser(userId, from, size);
        log.info("Events published by user: {}", response);
        return response;
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEventFullInfo(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId
    ) {
        log.info("GET request for full Event {} info received from user {}", eventId, userId);
        EventFullDto response = eventService.getEventFullInfo(userId, eventId);
        log.info("{}", response);
        return response;
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateEventUserRequest(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId,
            @RequestBody @Valid UpdateEventUserRequest request
            ) {
        log.info("PATCH | UserId: {} | EventId: {} | Update Event: {}", userId, eventId, request);
        EventFullDto response = eventService.updateEventRequest(eventId, request);
        log.info("Updated Event: {}", response);
        return response;
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId
    ) {
        log.info("GET | UserId: {} | EventId: {} | Participation Requests", userId, eventId);
        List<ParticipationRequestDto> response = eventService.getParticipationRequests(userId, eventId);
        log.info("Participation Requests: {}", response);
        return response;
    }

    @PatchMapping(path = "/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatusesOfRequestsForEvent(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "eventId") long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        log.info("PATCH | Update status requests");
        EventRequestStatusUpdateResult response
                = eventService.updateStatusesOfRequestsForEvent(userId, eventId, eventRequestStatusUpdateRequest);
        log.info("Update status requests: {}", response);
        return response;
    }

}
