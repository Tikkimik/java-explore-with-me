package ru.practicum.ewm.stats.hit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {

    private String api;

    private String uri;

    private String ip;

    private String timestamp;

}
