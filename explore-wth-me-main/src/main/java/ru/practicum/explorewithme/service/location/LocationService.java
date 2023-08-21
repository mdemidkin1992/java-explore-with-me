package ru.practicum.explorewithme.service.location;

import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.NewLocationDtoAdmin;
import ru.practicum.explorewithme.dto.location.LocationDtoWithEvents;

import java.util.List;

public interface LocationService {

    LocationDto addNewLocation(NewLocationDtoAdmin locationDto);

    List<LocationDtoWithEvents> getLocations(int from, int size);

}
