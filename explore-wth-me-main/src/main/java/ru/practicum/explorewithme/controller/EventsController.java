package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class EventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") @Min(0) int from,
            @RequestParam(required = false, defaultValue = "10") @Min(0) int size,
            @Value("${app-name}") String app,
            HttpServletRequest request
    ) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start must be before end");
        }
        log.info("GET | Get Events Public");
        List<EventShortDto> response = eventService.getEvents(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, app, request
        );
        log.info("Get Events Public: {}", response);
        return response;
    }

    @GetMapping(path = "/{id}")
    public EventFullDto getEvent(
            @PathVariable(name = "id") long eventId,
            @Value("${app-name}") String app,
            HttpServletRequest request
    ) {
        log.info("GET | Event with id {}", eventId);
        EventFullDto response = eventService.getEvent(eventId, app, request);
        log.info("{}", response);
        return response;
    }

}

