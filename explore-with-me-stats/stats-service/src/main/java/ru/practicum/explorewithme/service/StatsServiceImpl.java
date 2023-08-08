package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.controller.exception.LocalDateTimeException;
import ru.practicum.explorewithme.dto.StatDto;
import ru.practicum.explorewithme.dto.StatDtoWithHits;
import ru.practicum.explorewithme.model.Stat;
import ru.practicum.explorewithme.model.StatMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public StatDto saveStats(StatDto statDto) {
        Stat stat = StatMapper.fromStatDto(statDto);
        return StatMapper.toStatDto(statsRepository.save(stat));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDtoWithHits> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        checkLocalDateTimeOrThrow(start, end);
        List<StatDtoWithHits> ans = new ArrayList<>();

        for (String u : uris) {
            List<StatDtoWithHits> uriStats;
            if (!unique) {
                uriStats = statsRepository.getStatsForTimeInterval(start, end, List.of(u));
            } else {
                uriStats = statsRepository.getStatsForTimeIntervalUnique(start, end, List.of(u));
            }
            ans.addAll(uriStats);
        }

        ans.sort(Comparator.comparing(StatDtoWithHits::getHits).reversed());

        return ans;
    }

    private void checkLocalDateTimeOrThrow(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || end == null) {
            throw new LocalDateTimeException("Start must be after end");
        }
    }
}

