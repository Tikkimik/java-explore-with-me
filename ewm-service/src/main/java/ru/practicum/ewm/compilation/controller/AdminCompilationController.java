package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Create compilation.");
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Delete compilation by compId={}.", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void createCompilationEvent(@PathVariable Long compId,
                                       @PathVariable Long eventId) {
        log.info("Create compilation event by compId={}, eventId={}.", compId, eventId);
        compilationService.createCompilationEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteCompilationEvent(@PathVariable Long compId,
                                       @PathVariable Long eventId) {
        log.info("Delete compilation event by compId={}, eventId={}.", compId, eventId);
        compilationService.deleteCompilationEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Pin compilation by compId={}.", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Unpin compilation by compId={}.", compId);
        compilationService.unpinCompilation(compId);
    }
}