package ru.practicum.ewm.hit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReturnStatDto {

    private String app;

    private String uri;

    private Long hits;

}
