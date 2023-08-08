package ru.practicum.explorewithme.service.event;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.event.mapper.EventMapper;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.request.mapper.RequestMapper;
import ru.practicum.explorewithme.model.*;
import ru.practicum.explorewithme.model.enums.EventState;
import ru.practicum.explorewithme.model.enums.RequestStatus;
import ru.practicum.explorewithme.repository.*;
import ru.practicum.explorewithme.util.exception.ClientErrorException;
import ru.practicum.explorewithme.util.exception.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl extends UpdateEventOperations implements EventService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatsServiceClient statsServiceClient;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsByUser(long userId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        List<Event> eventsByUserId = eventRepository.findAllByInitiatorId(userId, page);
        List<EventShortDto> response = EventMapper.mapToEventShortDto(eventsByUserId);

        for (EventShortDto dto : response) {
            int confirmedRequests = getConfirmedRequests(dto.getId());
            dto.setConfirmedRequests(confirmedRequests);
        }

        return response;
    }

    @Override
    @Transactional
    public EventFullDto addNewEvent(long userId, NewEventDto newEventDto) {
        Event request = EventMapper.mapFromNewEventDto(newEventDto);

        Category category = getCategoryByIdOrThrowException(newEventDto.getCategory());
        request.setCategory(category);

        User initiator = getUserByIdOrThrowException(userId);
        request.setInitiator(initiator);
        request.setCreatedOn(LocalDateTime.now());
        request.setState(EventState.PENDING);

        Location location = locationRepository.save(newEventDto.getLocation());
        request.setLocation(location);

        Event response = eventRepository.save(request);
        return EventMapper.mapEventFullDtoFromEntity(response);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventFullInfo(long userId, long eventId) {
        Event response = getEventByIdOrThrowException(eventId);
        return EventMapper.mapEventFullDtoFromEntity(response);
    }

    @Override
    @Transactional
    public EventFullDto updateEventRequest(long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event existingEvent = entityManager.find(Event.class, eventId);
        checkIfEventIsAlreadyPublished(existingEvent);
        updateEvent(existingEvent, updateEventUserRequest);
        entityManager.merge(existingEvent);
        return EventMapper.mapEventFullDtoFromEntity(existingEvent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequests(long userId, long eventId) {
        List<ParticipationRequest> response
                = requestRepository.findAllByEventInitiatorIdAndEventId(userId, eventId);
        return RequestMapper.mapToDto(response);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByAdmin(List<Long> users,
                                               List<EventState> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               int from,
                                               int size
    ) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("eventDate").descending());
        List<Event> eventList = eventRepository
                .findEventsByAdmin(users, states, categories, rangeStart, rangeEnd, page);
        List<EventFullDto> response = EventMapper.mapToEventFullDto(eventList);

        for (EventFullDto dto : response) {
            int confirmedRequests = getConfirmedRequests(dto.getId());
            dto.setConfirmedRequests(confirmedRequests);
        }

        return response;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatusesOfRequestsForEvent(
            long userId,
            long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        RequestStatus status = eventRequestStatusUpdateRequest.getStatus();
        Event event = getEventByIdOrThrowException(eventId);

        int participationLimit = event.getParticipantLimit();
        boolean requestModeration = event.getRequestModeration();
        int confirmedParticipants = getConfirmedRequests(eventId);

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (participationLimit == 0 || !requestModeration) {
            updateRequestsStatusWhenModerationNotRequired(
                    requestIds,
                    entityManager
            );
        } else {
            updateRequestsStatusWhenModerationRequired(
                    requestIds,
                    entityManager,
                    confirmedParticipants,
                    participationLimit,
                    confirmedRequests,
                    rejectedRequests,
                    status
            );
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    private void updateRequestsStatusWhenModerationNotRequired(
            List<Long> requestIds,
            EntityManager entityManager
    ) {
        for (Long requestId : requestIds) {
            ParticipationRequest existingRequest
                    = entityManager.find(ParticipationRequest.class, requestId);

            existingRequest.setStatus(RequestStatus.CONFIRMED);
            entityManager.merge(existingRequest);
        }
    }

    private void updateRequestsStatusWhenModerationRequired(
            List<Long> requestIds,
            EntityManager entityManager,
            int confirmedParticipants,
            int participationLimit,
            List<ParticipationRequestDto> confirmedRequests,
            List<ParticipationRequestDto> rejectedRequests,
            RequestStatus status
    ) {
        for (Long requestId : requestIds) {
            ParticipationRequest existingRequest
                    = entityManager.find(ParticipationRequest.class, requestId);

            if (existingRequest.getStatus().equals(RequestStatus.CONFIRMED)
                    && status.equals(RequestStatus.REJECTED)) {
                throw new ClientErrorException("Request can't be REJECTED, has already been CONFIRMED");
            }

            if (confirmedParticipants < participationLimit) {
                setStatusIfLimitNotReached(
                        existingRequest,
                        entityManager,
                        confirmedRequests,
                        rejectedRequests,
                        status
                );
                if (status.equals(RequestStatus.CONFIRMED)) {
                    confirmedParticipants++;
                }
            } else {
                rejectIfLimitReached(
                        existingRequest,
                        entityManager,
                        rejectedRequests,
                        status
                );
            }
        }
    }

    private void setStatusIfLimitNotReached(
            ParticipationRequest existingRequest,
            EntityManager entityManager,
            List<ParticipationRequestDto> confirmedRequests,
            List<ParticipationRequestDto> rejectedRequests,
            RequestStatus status
    ) {
        if (existingRequest.getStatus().equals(RequestStatus.PENDING)) {
            existingRequest.setStatus(status);
            entityManager.merge(existingRequest);
            if (Objects.requireNonNull(status) == RequestStatus.CONFIRMED) {
                confirmedRequests.add(RequestMapper.mapToDto(existingRequest));
            } else {
                rejectedRequests.add(RequestMapper.mapToDto(existingRequest));
            }
        } else {
            throw new ClientErrorException("RequestStatus is not PENDING");
        }
    }

    private void rejectIfLimitReached(
            ParticipationRequest existingRequest,
            EntityManager entityManager,
            List<ParticipationRequestDto> rejectedRequests,
            RequestStatus status
    ) {
        if (status.equals(RequestStatus.CONFIRMED)) {
            throw new ClientErrorException("Limit has been reached");
        } else {
            existingRequest.setStatus(RequestStatus.REJECTED);
            entityManager.merge(existingRequest);
            rejectedRequests.add(RequestMapper.mapToDto(existingRequest));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEvents(String text,
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
    ) {
        Sort sorting = Sort.by("eventDate").descending();
        Pageable page = PageRequest.of(from / size, size, sorting);

        List<Event> eventList = eventRepository.findEventsByPublicUsers(
                text != null ? text.toLowerCase() : null,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                page
        );

        List<EventShortDto> response = EventMapper.mapToEventShortDto(eventList);

        for (EventShortDto eventShortDto : response) {
            long eventId = eventShortDto.getId();
            eventShortDto.setConfirmedRequests(getConfirmedRequests(eventId));
            eventShortDto.setViews(getViewsStats(eventId));
        }

        if (sort != null && sort.equals("VIEWS")) {
            response.sort(Comparator.comparing(EventShortDto::getViews));
        } else if (sort != null && sort.equals("EVENT_DATE")) {
            response.sort(Comparator.comparing(EventShortDto::getEventDate));
        }

        statsServiceClient.sendHit(app, request);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(long eventId, String app, HttpServletRequest request) {
        statsServiceClient.sendHit(app, request);
        Event event = getEventByIdOrThrowException(eventId);
        checkIfEventIsPublishedForPublic(event);
        EventFullDto response = EventMapper.mapEventFullDtoFromEntity(event);
        response.setConfirmedRequests(getConfirmedRequests(eventId));
        response.setViews(getViewsStats(eventId));
        return response;
    }

    private int getConfirmedRequests(long id) {
        return requestRepository.findAllByEventIdAndStatus(id, RequestStatus.CONFIRMED).size();
    }

    private int getViewsStats(long eventId) {
        ResponseEntity<Object> statsObject = statsServiceClient.getStatistics(eventId);
        return statsServiceClient.getHits(statsObject);
    }

    private Event getEventByIdOrThrowException(long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Event with id: " + eventId + " was not found")
        );
    }

    private Category getCategoryByIdOrThrowException(long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Category with id: " + catId + " was not found")
        );
    }

    private User getUserByIdOrThrowException(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " was not found")
        );
    }

    private void checkIfEventIsAlreadyPublished(@NonNull Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ClientErrorException("Published event can't be updated");
        }
    }

    private void checkIfEventIsPublishedForPublic(@NotNull Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EntityNotFoundException("Event is not published");
        }
    }

}
