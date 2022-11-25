package ru.practicum.ewm_stats.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm_stats.stats.dto.CreateStat;
import ru.practicum.ewm_stats.stats.repository.StatRepository;

import static ru.practicum.ewm_stats.stats.mapper.StatMapper.mapToStat;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public void addStat(CreateStat createStat) {
        statRepository.save(mapToStat(createStat));
    }
}
