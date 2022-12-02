package ru.practicum.ewm.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    //public
    List<CompilationDto> getCompilations(boolean pinned, Pageable pageable);

    CompilationDto getCompilation(Long compId);

    //admin
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    void createCompilationEvent(Long compId, Long eventId);

    void deleteCompilationEvent(Long compId, Long eventId);

    void pinCompilation(Long compId);

    void unpinCompilation(Long compId);

}
