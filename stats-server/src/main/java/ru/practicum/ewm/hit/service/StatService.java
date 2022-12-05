package ru.practicum.ewm.hit.service;

import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;

import java.util.List;

public interface StatService {

    void addStat(CreateStatDto createStatDto);

    List<ReturnStatDto> get(String start, String end, List<String> uris, boolean unique);

}
