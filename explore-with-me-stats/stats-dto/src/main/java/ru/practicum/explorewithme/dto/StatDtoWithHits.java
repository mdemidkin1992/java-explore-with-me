package ru.practicum.explorewithme.dto;

import lombok.*;

@Getter
@Setter
@ToString
public class StatDtoWithHits {
    private String app;
    private String uri;
    private Long hits;

    public StatDtoWithHits(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
