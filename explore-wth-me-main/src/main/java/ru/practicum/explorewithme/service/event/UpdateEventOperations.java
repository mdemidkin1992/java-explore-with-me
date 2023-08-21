package ru.practicum.explorewithme.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.location.LocationDtoUser;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Location;
import ru.practicum.explorewithme.model.enums.EventState;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.LocationRepository;
import ru.practicum.explorewithme.util.exception.ClientErrorException;

import java.time.LocalDateTime;
import java.util.Optional;

public class UpdateEventOperations {

    @Autowired
    protected CategoryRepository categoryRepository;
    @Autowired
    protected LocationRepository locationRepository;

    protected void updateEvent(Event existingEvent, UpdateEventUserRequest updateEventRequest) {
        if (existingEvent != null) {
            if (updateEventRequest.getAnnotation() != null) {
                existingEvent.setAnnotation(updateEventRequest.getAnnotation());
            }
            if (updateEventRequest.getCategory() != null) {
                Optional<Category> category = categoryRepository.findById(updateEventRequest.getCategory());
                category.ifPresent(existingEvent::setCategory);
            }
            if (updateEventRequest.getDescription() != null) {
                existingEvent.setDescription(updateEventRequest.getDescription());
            }
            if (updateEventRequest.getEventDate() != null) {
                existingEvent.setEventDate(updateEventRequest.getEventDate());
            }
            if (updateEventRequest.getLocation() != null) {
//                existingEvent.setLocation(updateEventRequest.getLocation());
//                locationRepository.save(updateEventRequest.getLocation());
                existingEvent.setLocation(updateLocation(existingEvent, updateEventRequest.getLocation()));
            }
            if (updateEventRequest.getPaid() != null) {
                existingEvent.setPaid(updateEventRequest.getPaid());
            }
            if (updateEventRequest.getParticipantLimit() != null) {
                existingEvent.setParticipantLimit(updateEventRequest.getParticipantLimit());
            }
            if (updateEventRequest.getRequestModeration() != null) {
                existingEvent.setRequestModeration(updateEventRequest.getRequestModeration());
            }
            if (updateEventRequest.getStateAction() != null) {
                updateEventState(existingEvent, updateEventRequest);
            }
            if (updateEventRequest.getTitle() != null) {
                existingEvent.setTitle(updateEventRequest.getTitle());
            }
        }
    }

    protected void updateEventState(Event existingEvent, UpdateEventUserRequest updateEventRequest) {
        if (updateEventRequest.getStateAction().equals("SEND_TO_REVIEW")) {
            existingEvent.setState(EventState.PENDING);
        } else if (updateEventRequest.getStateAction().equals("PUBLISH_EVENT")) {
            if (existingEvent.getState().equals(EventState.PUBLISHED)
                    || existingEvent.getState().equals(EventState.CANCELED)) {
                throw new ClientErrorException("For the requested operation the conditions are not met.");
            } else {
                existingEvent.setState(EventState.PUBLISHED);
                existingEvent.setPublishedOn(LocalDateTime.now());
            }
        } else {
            if (existingEvent.getState().equals(EventState.PUBLISHED)) {
                throw new ClientErrorException("For the requested operation the conditions are not met.");
            } else {
                existingEvent.setState(EventState.CANCELED);
            }
        }
    }

    private Location updateLocation(Event existingEvent, LocationDtoUser locationDtoUser) {
        Double newLat = locationDtoUser.getLat();
        Double newLon = locationDtoUser.getLon();
        Location existingLocation = existingEvent.getLocation();
        existingLocation.setLat(newLat);
        existingLocation.setLon(newLon);
        return locationRepository.save(existingLocation);
    }
}
