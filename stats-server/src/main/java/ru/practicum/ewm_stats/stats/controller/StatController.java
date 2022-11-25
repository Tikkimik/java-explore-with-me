package ru.practicum.ewm_stats.stats.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm_stats.stats.dto.CreateStat;
import ru.practicum.ewm_stats.stats.service.StatService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class StatController {

    private final StatService statService;

    @PostMapping
    @RequestMapping("/hit")
    public void addStat(@Valid @RequestBody CreateStat createStat) {
        statService.addStat(createStat);
    }

}
