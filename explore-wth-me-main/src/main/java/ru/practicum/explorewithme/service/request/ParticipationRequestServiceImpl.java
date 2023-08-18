package ru.practicum.explorewithme.service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.request.mapper.RequestMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.ParticipationRequest;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.model.enums.EventState;
import ru.practicum.explorewithme.model.enums.RequestStatus;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.util.exception.ClientErrorException;
import ru.practicum.explorewithme.util.exception.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getRequestsByUserId(long userId) {
        return repository.findAllByRequesterId(userId).stream()
                .map(RequestMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto addNewParticipationRequest(long userId, long eventId) {
        makeParticipationRequestChecks(userId, eventId);
        User requester = getUserByIdOrThrowException(userId);
        Event event = getEventByIdOrThrowException(eventId);

        ParticipationRequest request = buildParticipationRequest(requester, event);
        ParticipationRequest response = repository.save(request);
        return RequestMapper.mapToDto(response);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(long userId, long requestId) {
        ParticipationRequest existingRequest = getParticipationRequestOrThrow(requestId);
        existingRequest.setStatus(RequestStatus.CANCELED);
        repository.save(existingRequest);
        return RequestMapper.mapToDto(existingRequest);
    }

    private Event getEventByIdOrThrowException(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Event with id: " + eventId + " was not found")
        );
    }

    private User getUserByIdOrThrowException(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " was not found")
        );
    }

    private ParticipationRequest getParticipationRequestOrThrow(long requestId) {
        return repository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException("Request with id not found")
        );
    }

    private ParticipationRequest buildParticipationRequest(User requester, Event event) {
        RequestStatus status;

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }

        return ParticipationRequest.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .status(status)
                .build();
    }

    private void makeParticipationRequestChecks(long userId, long eventId) {
        checkIfParticipationRequestExists(userId, eventId);
        checkIfRequesterIsTheEventInitiator(userId, eventId);
        checkIfEventIsPublished(eventId);
        checkIfParticipationLimitIsOK(eventId);
    }

    private void checkIfParticipationRequestExists(long userId, long eventId) {
        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ClientErrorException("Participation request already exists");
        }
    }

    private void checkIfRequesterIsTheEventInitiator(long userId, long eventId) {
        Event event = getEventByIdOrThrowException(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ClientErrorException("Participant can't be event initiator");
        }
    }

    private void checkIfEventIsPublished(long eventId) {
        Event event = getEventByIdOrThrowException(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ClientErrorException("Event state must be published");
        }
    }

    private void checkIfParticipationLimitIsOK(long eventId) {
        Event event = getEventByIdOrThrowException(eventId);
        int confirmedRequests = repository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();
        int participationLimit = event.getParticipantLimit();

        if (confirmedRequests >= participationLimit && participationLimit != 0) {
            throw new ClientErrorException("Participation limit is exceeded");
        }
    }

}
