package ru.practicum.ewm.hit.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.model.Stats;

@Component
@AllArgsConstructor
public class StatMapper {

    public static Stats toStats(CreateStatDto statDto) {
        return new Stats(
                statDto.getApp(),
                statDto.getUri(),
                statDto.getIp(),
                statDto.getTimestamp());
    }


    public static ReturnStatDto returnStatDto(Stats stats, Long hits) {
        return new ReturnStatDto(stats.getApp(), stats.getUri(), hits);
    }
}