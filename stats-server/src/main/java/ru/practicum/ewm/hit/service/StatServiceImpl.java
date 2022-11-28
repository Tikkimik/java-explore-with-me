package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.mapper.StatMapper;
import ru.practicum.ewm.hit.model.Stats;
import ru.practicum.ewm.hit.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;
    private final StatMapper statMapper;

    @Override
    public void addStat(CreateStatDto createStat) {
        statRepository.save(statMapper.toStats(createStat));
    }

    @Override
    public List<ReturnStatDto> get(String start, String end, List<String> uris, boolean unique) {
        List<Stats> returnStat;
        Map<String, Stats> uniqueIpAddress;
        LocalDateTime starts = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime ends = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (uris == null) {
            returnStat = statRepository.getHitsByTimestampAfterAndTimestampBefore(starts, ends);
        } else {
            returnStat = statRepository.getHitsByTimestampAfterAndTimestampBeforeAndUriIn(starts, ends, uris);
        }

        if (unique) {
            uniqueIpAddress = new HashMap<>();
            returnStat.forEach(stats -> uniqueIpAddress.put(stats.getIp(), stats));
            returnStat = new ArrayList<>(uniqueIpAddress.values());
        }

        return returnStat.stream()
                .map(statMapper::returnStatDto)
                .collect(Collectors.toList());
    }
}