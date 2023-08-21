package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.location.LocationDtoWithEvents;
import ru.practicum.explorewithme.service.location.LocationService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/locations")
public class LocationsController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationDtoWithEvents> getLocations(
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("GET | Locations");
        List<LocationDtoWithEvents> response = locationService.getLocations(from, size);
        log.info("{}", response);
        return response;
    }

}
