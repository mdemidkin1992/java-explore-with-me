package ru.practicum.explorewithme.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.model.Stat;
import ru.practicum.explorewithme.model.StatMapper;
import ru.practicum.explorewithme.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @InjectMocks
    private StatsServiceImpl statsService;
    @Mock
    private StatsRepository statsRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LocalDateTime START = LocalDateTime.parse("2020-01-01 10:00:00", FORMATTER);
    private static final LocalDateTime END = LocalDateTime.parse("2035-01-01 10:00:00", FORMATTER);


    private StatDto statDto;
    private Stat stat;

    @BeforeEach
    void init() {
        statDto = StatDto.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("121.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        stat = StatMapper.fromStatDto(statDto);
    }

    @Test
    void saveStats() {
        when(statsRepository.save(any())).thenReturn(stat);

        StatDto expected = StatMapper.toStatDto(stat);
        StatDto actual = statsService.saveStats(statDto);
        assertEquals(expected, actual);
    }

    @Test
    void getStats() {
        int hits = 1;
        StatDtoWithHits statDtoWithHits = new StatDtoWithHits(stat.getApp(), stat.getUri(), hits);
//        List<String> emptyList = Collections.emptyList();

        when(statsRepository.getStatsForTimeIntervalUnique(any(), any(), any())).thenReturn(List.of(statDtoWithHits));
        when(statsRepository.getStatsForTimeInterval(any(), any(), any())).thenReturn(List.of(statDtoWithHits));
//        when(statsRepository.getStatsForTimeInterval(any(), any(), emptyList)).thenReturn(List.of(statDtoWithHits));
//        when(statsRepository.getStatsForTimeIntervalUnique(any(), any(), emptyList)).thenReturn(List.of(statDtoWithHits));

        List<StatDtoWithHits> expected = List.of(statDtoWithHits);
        List<String> uris = List.of(stat.getUri());
//        assertEquals(expected, statsService.getStats(START, END, emptyList, false));
        assertEquals(expected, statsService.getStats(START, END, uris, false));
//        assertEquals(expected, statsService.getStats(START, END, emptyList, true));
        assertEquals(expected, statsService.getStats(START, END, uris, true));
    }

}