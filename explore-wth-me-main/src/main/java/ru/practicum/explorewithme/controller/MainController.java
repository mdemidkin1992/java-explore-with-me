package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class MainController {

    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<Object> getEvents(
            @Value("${app-name}") String app,
            HttpServletRequest request,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info("GET request for events received from Main client 8080");
        ResponseEntity<Object> savedHit = statsClient.sendHit(app, request);
        ResponseEntity<Object> stats = statsClient.getStats(start, end, uris, unique);
        log.info("Hit: {}, Stats: {}", savedHit, stats);
        return savedHit;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getEventById(@Value("${app-name}") String app,
                                               @PathVariable long id,
                                               HttpServletRequest request) {
        log.info("GET request for events received from Main client 8080");
        ResponseEntity<Object> savedHit = statsClient.sendHit(app, request);
        log.info("{}", savedHit);
        return savedHit;
    }

}

