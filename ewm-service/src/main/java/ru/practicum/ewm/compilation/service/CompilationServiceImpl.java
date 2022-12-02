package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.exceptions.UpdateException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.compilation.mapper.CompilationMapper.mapCompilationToCompilationDto;
import static ru.practicum.ewm.compilation.mapper.CompilationMapper.mapNewCompilationDtoToCompilation;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, Pageable pageable) {

        List<Compilation> list = compilationRepository.findAllByPinned(pinned, pageable).getContent();

        return list.stream()
                .map(CompilationMapper::mapCompilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IncorrectParameterException("Compilation not found by compId."));

        return mapCompilationToCompilationDto(compilation);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        Compilation compilation = mapNewCompilationDtoToCompilation(newCompilationDto,
                eventRepository.findAllById(newCompilationDto.getEvents()));

        return mapCompilationToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public void createCompilationEvent(Long compId, Long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IncorrectParameterException("Compilation not found by compId."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IncorrectParameterException("Event not found by eventId."));

        compilation.getEventList().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilationEvent(Long compId, Long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IncorrectParameterException("Compilation not found by compId."));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IncorrectParameterException("Event not found by eventId."));

        compilation.getEventList().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IncorrectParameterException("Compilation not found by compId."));

        if (compilation.isPinned())
            throw new UpdateException("Compilation has already been pinned.");

        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new IncorrectParameterException("Compilation not found by compId."));

        if (!compilation.isPinned())
            throw new UpdateException("Compilation has already been pinned.");

        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }
}