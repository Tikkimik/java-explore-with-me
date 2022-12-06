package ru.practicum.ewm.stats.hit;

import lombok.Getter;

@Getter
public class StatEvents {

    String app;

    String uri;

    Long hits;

    public StatEvents(Long hits, String uri, String app) {
        this.hits = hits;
        this.uri = uri;
        this.app = app;
    }
}
