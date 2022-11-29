package ru.practicum.ewm.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CompilationMapper {

    public static Compilation mapToCompilation(NewCompilationDto compilationDto) {
        return new Compilation(compilationDto.isPinned(), compilationDto.getTitle());
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), events, compilation.isPinned(), compilation.getTitle());
    }
}
