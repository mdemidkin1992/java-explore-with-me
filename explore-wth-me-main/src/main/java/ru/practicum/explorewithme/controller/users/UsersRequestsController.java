package ru.practicum.explorewithme.controller.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.service.request.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class UsersRequestsController {

    private final ParticipationRequestService participationRequestService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsByUserId(
        @PathVariable(name = "userId") long userId
    ) {
        log.info("GET | UserId: {} | Participation Requests", userId);
        List<ParticipationRequestDto> response
                = participationRequestService.getRequestsByUserId(userId);
        log.info("Participation Requests: {}", response);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addNewParticipationRequest(
            @PathVariable(name = "userId") long userId,
            @RequestParam(name = "eventId") long eventId
    ) {
        log.info("POST | UserId: {} | EventId: {} | Add Request", userId, eventId);
        ParticipationRequestDto response
                = participationRequestService.addNewParticipationRequest(userId, eventId);
        log.info("Add Request: {}", response);
        return response;
    }

    @PatchMapping(path = "/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(
            @PathVariable(name = "userId") long userId,
            @PathVariable(name = "requestId") long requestId
    ) {
        log.info("DELETE | UserId: {} | RequestId: {} | Cancel Request", userId, requestId);
        ParticipationRequestDto response
                = participationRequestService.cancelParticipationRequest(userId, requestId);
        log.info("Cancel Request: {}", response);
        return response;
    }

}
