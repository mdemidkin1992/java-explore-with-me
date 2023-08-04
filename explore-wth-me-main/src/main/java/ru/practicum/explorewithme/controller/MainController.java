package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.StatsClient;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class MainController {

    private final StatsClient statsClient;

    @GetMapping
    public ResponseEntity<Object> getEvents(HttpServletRequest request) {
        log.info("GET request for events received from Main client 8080");
        ResponseEntity<Object> response = statsClient.sendHit(request);
        log.info("{}", response);
        return response;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable long id, HttpServletRequest request) {
        log.info("GET request for events received from Main client 8080");
        ResponseEntity<Object> response = statsClient.sendHit(request);
        log.info("{}", response);
        return response;
    }

}

