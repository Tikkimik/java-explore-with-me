package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto getCompilation(Long compId);

    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    void createCompilationEvent(Long compId, Long eventId);

    void deleteCompilationEvent(Long compId, Long eventId);

    void pinCompilation(Long compId);

    void unpinCompilation(Long compId);

}
