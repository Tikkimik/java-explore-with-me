package ru.practicum.ewm.hit.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.model.Stats;
import ru.practicum.ewm.hit.repository.StatRepository;

@Component
@AllArgsConstructor
public class StatMapper {

    private StatRepository statRepository;

    public Stats toStats(CreateStatDto createStatDto) {
        return new Stats(createStatDto.getApp(),
                createStatDto.getUri(),
                createStatDto.getIp(),
                createStatDto.getTimestamp());
    }

    public ReturnStatDto returnStatDto(Stats stats) {
        return new ReturnStatDto(stats.getApp(),
                stats.getUri(),
                statRepository.countHitsByUri(stats.getUri()));
    }
}