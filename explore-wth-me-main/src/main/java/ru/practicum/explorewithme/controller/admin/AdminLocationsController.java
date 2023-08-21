package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.location.LocationDto;
import ru.practicum.explorewithme.dto.location.NewLocationDtoAdmin;
import ru.practicum.explorewithme.service.location.LocationService;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/locations")
public class AdminLocationsController {

    private final LocationService locationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto addNewLocation(@RequestBody @Valid NewLocationDtoAdmin locationDto) {
        log.info("POST | Add new Location");
        LocationDto response = locationService.addNewLocation(locationDto);
        log.info("{}", response);
        return response;
    }

}
