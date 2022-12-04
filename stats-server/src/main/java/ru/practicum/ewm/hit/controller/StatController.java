package ru.practicum.ewm.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.hit.dto.CreateStatDto;
import ru.practicum.ewm.hit.dto.ReturnStatDto;
import ru.practicum.ewm.hit.service.StatService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @PostMapping("/hit")
    public void addStat(@Valid @RequestBody CreateStatDto createStatDto) {
        log.info("add hit in stat service.");
        statService.addStat(createStatDto);
    }

    @PostMapping("/hit/sts")
    public Long getStatistic(@Valid @RequestBody CreateStatDto createStatDto) {
        log.info("get hit stat in stat.");
        return statService.getEventViewStat(createStatDto);
    }

    @PostMapping("/hit/stss")
    public List<Long> getStatisticsas(@Valid @RequestBody CreateStatDto createStatDto) {
        log.info("get hit stat in stat.");
        return statService.getEventsViewStat(createStatDto);
    }

    @GetMapping("/stats")
    public List<ReturnStatDto> get(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam List<String> uris,
                                   @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info("get stat from stat service with start={}, end={}, uris={}, unique={}.", start, end, uris, unique);
        return statService.get(start, end, uris, unique);
    }
}