package ru.practicum.explorewithme.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.LocationUpdateRequest;
import ru.practicum.explorewithme.dto.location.NewLocationDtoAdmin;
import ru.practicum.explorewithme.dto.location.LocationDtoWithEvents;
import ru.practicum.explorewithme.dto.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.Location;
import ru.practicum.explorewithme.model.enums.EventState;
import ru.practicum.explorewithme.model.enums.LocationStatus;
import ru.practicum.explorewithme.repository.LocationRepository;
import ru.practicum.explorewithme.util.exception.ClientErrorException;
import ru.practicum.explorewithme.util.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public LocationDto addNewLocation(NewLocationDtoAdmin locationDto) {
        Location request = LocationMapper.mapFromLocationDto(locationDto);
        checkIfLocationExistsOrThrowException(locationDto);
        request.setStatus(LocationStatus.APPROVED_BY_ADMIN);
        Location response = locationRepository.save(request);
        return LocationMapper.mapFullDtoFromEntity(response);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDtoWithEvents> getLocations(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);

        List<Location> locations = locationRepository.findAllByNameNotNull(page);
        List<LocationDtoWithEvents> response = new ArrayList<>();

        for (Location location : locations) {
            String locationName = location.getName();
            List<Event> eventList = location.getEventList();

            long eventsCount = eventList.stream()
                    .filter(e -> e.getState().equals(EventState.PUBLISHED))
                    .count();

            response.add(new LocationDtoWithEvents(locationName, eventsCount));
        }

        response.sort(Comparator.comparing(LocationDtoWithEvents::getEvents).reversed());
        return response;
    }

    @Override
    @Transactional
    public LocationDto updateLocation(long id, LocationUpdateRequest request) {
        Location existingLocation = getLocationOrThrowException(id);
        if (request.getStatus().equals("APPROVED")) {
            existingLocation.setStatus(LocationStatus.APPROVED_BY_ADMIN);

            if (request.getName() != null) {
                existingLocation.setName(request.getName());
            }
            if (request.getRad() != null) {
                existingLocation.setRad(request.getRad());
            }

        } else {
            existingLocation.setStatus(LocationStatus.CANCELED_BY_ADMIN);
        }
        Location updatedLocation = locationRepository.save(existingLocation);
        return LocationMapper.mapFullDtoFromEntity(updatedLocation);
    }

    private void checkIfLocationExistsOrThrowException(NewLocationDtoAdmin dto) {
        if (locationRepository.existsByNameAndLatAndLon(
            dto.getName(), dto.getLat(), dto.getLon()
        )) {
            throw new ClientErrorException("Location already exists");
        }
    }

    private Location getLocationOrThrowException(long locationId) {
        return locationRepository.findById(locationId).orElseThrow(
                () -> new EntityNotFoundException("Location not found")
        );
    }

}
