package ru.practicum.explorewithme.model;

import lombok.experimental.UtilityClass;
import ru.practicum.explorewithme.dto.StatDto;

@UtilityClass
public class StatMapper {

    public Stat fromStatDto(StatDto statDto) {
        return Stat.builder()
                .app(statDto.getApp())
                .uri(Uri.builder().name(statDto.getUri()).build())
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

}