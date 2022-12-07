package ru.practicum.ewm.stats.hit;

import lombok.Getter;

@Getter
public class Stat {

    private Long id;

    private Long hits;

    private String uri;

    private String app;

    public Stat(Long hits, String uri, String app) {
        this.hits = hits;
        this.uri = uri;
        this.app = app;
        this.id = Long.parseLong(uri.substring(8));
    }

    @Override
    public String toString() {
        return "Stat{" +
                "id=" + id +
                ", hits=" + hits +
                ", uri='" + uri + '\'' +
                ", app='" + app + '\'' +
                '}';
    }
}
