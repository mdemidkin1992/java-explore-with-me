package ru.practicum.explorewithme.service;


import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    StatDto saveStats(StatDto statDto);

    List<StatDtoWithHits> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);

    StatDto getStatsById(long id);

}
