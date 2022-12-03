package ru.practicum.ewm.hit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnStatDto {

    private String app;

    private String uri;

    private Long hits;

}
