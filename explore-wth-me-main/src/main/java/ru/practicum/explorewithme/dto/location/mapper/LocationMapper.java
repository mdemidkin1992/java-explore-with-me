package ru.practicum.explorewithme.dto.location.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.NewLocationDtoAdmin;
import ru.practicum.explorewithme.dto.location.NewCoordinatesDto;
import ru.practicum.explorewithme.model.Location;

@UtilityClass
public class LocationMapper {

    public Location mapFromLocationDto(NewLocationDtoAdmin dto) {
        return Location.builder()
                .name(dto.getName())
                .lon(dto.getLon())
                .lat(dto.getLat())
                .rad(dto.getRad())
                .build();
    }

    public Location mapFromLocationShortDto(NewCoordinatesDto dto) {
        return Location.builder()
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public LocationDto mapFullDtoFromEntity(Location entity) {
        return LocationDto.builder()
                .id(entity.getId())
                .name((entity.getName()))
                .lat(entity.getLat())
                .lon(entity.getLon())
                .rad(entity.getRad())
                .status(entity.getStatus())
                .build();
    }

}
