package ru.practicum.ewm.client.hit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HitDto {

    private String api;

    private String uri;

    private String ip;

    private String timestamp;

}
