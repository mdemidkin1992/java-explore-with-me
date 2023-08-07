package ru.practicum.explorewithme.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatDtoWithHits {
    private String app;
    private String uri;
    private long hits;

    public StatDtoWithHits(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
