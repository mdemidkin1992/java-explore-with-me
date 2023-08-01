package ru.practicum.explorewithme.model;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.StatDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class StatMapper {

    public Stat fromStatDto(StatDto statDto) {
        return Stat.builder()
                .app(statDto.getApp())
                .uri(Uri.builder().name(statDto.getUri()).build())
//                .uri(statDto.getUri())
                .ip(statDto.getIp())
                .timestamp(statDto.getTimestamp())
                .build();
    }

    public StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri().getName())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp())
                .build();
    }

    public List<StatDto> toStatDto(List<Stat> stats) {
        return stats.stream()
                .map(StatMapper::toStatDto)
                .collect(Collectors.toList());
    }

}