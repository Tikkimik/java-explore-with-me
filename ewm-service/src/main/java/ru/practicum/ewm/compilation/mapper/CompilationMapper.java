package ru.practicum.ewm.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CompilationMapper {

    public static Compilation mapNewCompilationDtoToCompilation(NewCompilationDto newCompilationDto, List<Event> list) {
        Compilation compilation = new Compilation();

        compilation.setTitle(newCompilationDto.getTitle());
        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setEventList(list);
        return compilation;
    }

    public static CompilationDto mapCompilationToCompilationDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();

        dto.setId(compilation.getId());
        dto.setTitle(compilation.getTitle());
        dto.setPinned(compilation.isPinned());

        if (compilation.getEventList() != null) {
            List<EventShortDto> list = compilation.getEventList().stream()
                    .map(EventMapper::toEventShortDto)
                    .collect(Collectors.toList());
            dto.setEvents(list);
        } else {
            dto.setEvents(Collections.emptyList());
        }

        return dto;
    }
}