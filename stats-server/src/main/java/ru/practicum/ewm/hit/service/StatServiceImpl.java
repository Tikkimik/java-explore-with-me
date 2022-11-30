package ru.practicum.ewm.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.model.Stats;
import ru.practicum.ewm.hit.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.hit.mapper.StatMapper.returnStatDto;
import static ru.practicum.ewm.hit.mapper.StatMapper.toStats;

@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    @Override
    public void addStat(CreateStatDto createStat) {
        statRepository.save(toStats(createStat));
    }

    @Override
    public List<ReturnStatDto> gets(String start, String end, List<String> uris, boolean unique) {
        List<Stats> returnStat;
        LocalDateTime starts = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime ends = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (unique) {
            returnStat = statRepository.findDistinctByTimestampBetweenAndUri(starts, ends, uris);

//            return getViewStatsWithHit(listUnique);
        } else {
            returnStat = statRepository.findAllByUriIn(uris);

//            return getViewStatsWithHit(list);
        }

        Set<ReturnStatDto> viewStatsSet = returnStat.stream()
                .map(this::ReturnStatDto viewStats = new ReturnStatDto();
        viewStats.setApp(hit.getApp());
        viewStats.setUri(hit.getUri());
        return viewStats;)
                .collect(Collectors.toSet());



    }

    @Override
    public List<ReturnStatDto> get(String start, String end, List<String> uris, boolean unique) {
        List<Stats> returnStat;

        List<ReturnStatDto> returnStatDto = new ArrayList<>();

        Map<String, Stats> uniqueIpAddress;

        LocalDateTime starts = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime ends = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (uris == null) {
            returnStat = statRepository.getStatsByTimestampAfterAndTimestampBefore(starts, ends);
        } else {
            returnStat = statRepository.getStatsByTimestampAfterAndTimestampBeforeAndUriIn(starts, ends, uris);
        }

        if (unique) {
            uniqueIpAddress = new HashMap<>();
            returnStat.forEach(stats -> uniqueIpAddress.put(stats.getIp(), stats));
            returnStat = new ArrayList<>(uniqueIpAddress.values());
        }

        for (Stats stats : returnStat) {
            returnStatDto.add(returnStatDto(stats, statRepository.countStatsByUri(stats.getUri())));
        }

        return returnStatDto;
    }
}