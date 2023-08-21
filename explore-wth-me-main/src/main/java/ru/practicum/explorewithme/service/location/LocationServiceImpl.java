package ru.practicum.explorewithme.service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.NewLocationDtoAdmin;
import ru.practicum.explorewithme.dto.location.LocationDtoWithEvents;
import ru.practicum.explorewithme.dto.location.mapper.LocationMapper;
import ru.practicum.explorewithme.model.Location;
import ru.practicum.explorewithme.model.enums.LocationStatus;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.LocationRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public LocationDto addNewLocation(NewLocationDtoAdmin locationDto) {
        Location request = LocationMapper.mapFromLocationDto(locationDto);
        request.setStatus(LocationStatus.APPROVED_BY_ADMIN);
        Location response = locationRepository.save(request);
        return LocationMapper.mapFullDtoFromEntity(response);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationDtoWithEvents> getLocations(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        List<Location> locations = locationRepository.findAll(page).toList();
        List<LocationDtoWithEvents> response = new ArrayList<>();

        for (Location location : locations) {
            String locationName = location.getName();
            long locationId = location.getId();
            long eventsCount = eventRepository.findAllByLocationId(locationId).size();
            response.add(new LocationDtoWithEvents(locationName, eventsCount));
        }

        response.sort(Comparator.comparing(LocationDtoWithEvents::getEvents));

        return response;
    }

}
