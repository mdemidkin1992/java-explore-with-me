package ru.practicum.explorewithme.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.model.Stat;
import ru.practicum.explorewithme.model.StatMapper;
import ru.practicum.explorewithme.model.Uri;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StatsRepositoryTest {

    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private UriRepository uriRepository;

    private StatDto statDto1, statDto2;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final LocalDateTime START = LocalDateTime.parse("2020-01-01 10:00:00", FORMATTER);
    private static final LocalDateTime END = LocalDateTime.parse("2035-01-01 10:00:00", FORMATTER);
    private final int hits = 1;

    @BeforeEach
    @SneakyThrows
    void init() {
        statDto1 = StatDto.builder()
                .app("ewm-main-service")
                .uri("/events")
                .ip("121.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        statDto2 = StatDto.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("121.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        StatDto statDto3 = statDto2;

        Uri uri1 = uriRepository.save(Uri.builder().name(statDto1.getUri()).build());
        Uri uri2 = uriRepository.save(Uri.builder().name(statDto2.getUri()).build());

        Stat stat1 = StatMapper.fromStatDto(statDto1);
        Stat stat2 = StatMapper.fromStatDto(statDto2);
        Stat stat3 = StatMapper.fromStatDto(statDto3);

        stat1.setUri(uri1);
        stat2.setUri(uri2);
        stat3.setUri(uri2);

        statsRepository.save(stat1);
        statsRepository.save(stat2);
        statsRepository.save(stat3);
    }

    @Test
    void getStatsForTimeIntervalAndUris() {
        final String[] uris = {"/events"};

        List<StatDtoWithHits> actual = statsRepository.getStatsForTimeIntervalAndUris(START, END, uris);
        List<StatDtoWithHits> expected = List.of(new StatDtoWithHits(statDto1.getApp(), statDto1.getUri(), hits));

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertThat(expected.get(0).getApp(), equalTo(actual.get(0).getApp()));
        assertThat(expected.get(0).getUri(), equalTo(actual.get(0).getUri()));
        assertThat(expected.get(0).getHits(), equalTo(actual.get(0).getHits()));
    }

    @Test
    void getStatsForTimeIntervalAndUrisUnique() {
        final String[] uris = {"/events/1"};

        List<StatDtoWithHits> actual = statsRepository.getStatsForTimeIntervalAndUrisUnique(START, END, uris);
        List<StatDtoWithHits> expected = List.of(new StatDtoWithHits(statDto2.getApp(), statDto2.getUri(), hits));

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertThat(expected.get(0).getApp(), equalTo(actual.get(0).getApp()));
        assertThat(expected.get(0).getUri(), equalTo(actual.get(0).getUri()));
        assertThat(expected.get(0).getHits(), equalTo(actual.get(0).getHits()));
    }

    @Test
    void getStatsForTimeInterval() {
        List<StatDtoWithHits> actual = statsRepository.getStatsForTimeInterval(START, END);
        List<StatDtoWithHits> expected = List.of(
                new StatDtoWithHits(statDto2.getApp(), statDto2.getUri(), hits + 1),
                new StatDtoWithHits(statDto1.getApp(), statDto1.getUri(), hits)
        );

        assertNotNull(expected);
        assertEquals(expected.size(), actual.size());
        assertThat(expected.get(0).getApp(), equalTo(actual.get(0).getApp()));
        assertThat(expected.get(0).getUri(), equalTo(actual.get(0).getUri()));
        assertThat(expected.get(0).getHits(), equalTo(actual.get(0).getHits()));
        assertThat(expected.get(1).getApp(), equalTo(actual.get(1).getApp()));
        assertThat(expected.get(1).getUri(), equalTo(actual.get(1).getUri()));
        assertThat(expected.get(1).getHits(), equalTo(actual.get(1).getHits()));
    }

    @Test
    void getStatsForTimeIntervalUnique() {
        List<StatDtoWithHits> actual = statsRepository.getStatsForTimeIntervalUnique(START, END);
        List<StatDtoWithHits> expected = List.of(
                new StatDtoWithHits(statDto1.getApp(), statDto1.getUri(), hits),
                new StatDtoWithHits(statDto2.getApp(), statDto2.getUri(), hits)
        );

        assertNotNull(expected);
        assertEquals(expected.size(), actual.size());
        assertThat(expected.get(0).getApp(), equalTo(actual.get(0).getApp()));
        assertThat(expected.get(0).getUri(), equalTo(actual.get(0).getUri()));
        assertThat(expected.get(0).getHits(), equalTo(actual.get(0).getHits()));
        assertThat(expected.get(1).getApp(), equalTo(actual.get(1).getApp()));
        assertThat(expected.get(1).getUri(), equalTo(actual.get(1).getUri()));
        assertThat(expected.get(1).getHits(), equalTo(actual.get(1).getHits()));
    }

    @AfterEach
    void clearDb() {
        statsRepository.deleteAll();
        uriRepository.deleteAll();
    }

}