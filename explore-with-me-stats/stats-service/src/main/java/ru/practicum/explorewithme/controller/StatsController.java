package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.service.StatsService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService service;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStats(@RequestBody StatDto statDto) {
        log.info("POST request for stats received: {}", statDto);
        StatDto response = service.saveStats(statDto);
        log.info("Stats saved: {}", response);
    }

    @GetMapping(path = "/stats")
    public List<StatDtoWithHits> getStats(@RequestParam String start,
                                          @RequestParam String end,
                                          @RequestParam(required = false) String[] uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("GET request for stats received from {} to {} with uris: {}", start, end, uris);
        List<StatDtoWithHits> response = service.getStats(start, end, uris, unique);
        log.info("{}", response);
        return response;
    }

    @GetMapping(path = "/stats/{id}")
    public StatDto getStatsById(@PathVariable long id) {
        log.info("GET request received for stats with id {}", id);
        StatDto response = service.getStatsById(id);
        log.info("{}", response);
        return response;
    }

}
