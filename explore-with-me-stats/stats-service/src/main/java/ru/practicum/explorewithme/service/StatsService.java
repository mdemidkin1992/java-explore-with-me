package ru.practicum.explorewithme.service;


import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;

import java.util.List;

public interface StatsService {

    StatDto saveStats(StatDto statDto);

//    List<StatDto> getStats(String start, String end, List<String> uris, boolean unique);

    List<StatDtoWithHits> getStats(String start, String end, String[] uris, Boolean unique);

//    List<StatDtoWithHits> getStatsForTimeInterval(LocalDateTime start, LocalDateTime end);

    StatDto getStatsById(long id);

}
