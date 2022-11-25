package ru.practicum.ewm_stats.stats.mapper;

import ru.practicum.ewm_stats.stats.dto.CreateStat;
import ru.practicum.ewm_stats.stats.model.Stats;

public class StatMapper {
    public static Stats mapToStat(CreateStat createStat) {
        Stats stat = new Stats();
        stat.setApp(createStat.getApp());
        stat.setIp(createStat.getIp());
        stat.setUri(createStat.getUri());
        stat.setTimestamp(createStat.getTimestamp());
        return stat;
    }


}
