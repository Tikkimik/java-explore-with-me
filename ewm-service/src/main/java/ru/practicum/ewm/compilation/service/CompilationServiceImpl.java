package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.*;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.repository.EventCompilationRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.exceptions.UpdateException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventCompilationRepository eventCompilationRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        compilation = compilationRepository.save(compilation);
        for (Long eventId : newCompilationDto.getEvents()) {
            EventCompilation eventCompilation = new EventCompilation(eventId, compilation.getId());
            eventCompilationRepository.save(eventCompilation);
        }
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        checkCompId(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public void deleteCompilationEvent(Long compId, Long eventId) {
        checkCompId(compId);
        EventCompilation ec = eventCompilationRepository.getByCompilationIdAndEventId(compId, eventId);

        System.out.println("deleteCompilationEvent" + ec.getCompilationId() + "<- com event ->" + ec.getEventId());
//        if (ec.isEmpty())
//            throw new IncorrectParameterException("there aren't this events id in this compilation");
        eventCompilationRepository.deleteById(ec.getId());
    }

    @Override
    @Transactional
    public void createCompilationEvent(Long compId, Long eventId) {
        checkCompId(compId);
        if (eventCompilationRepository.existsByCompilationIdAndEventId(compId, eventId))
            throw new IncorrectParameterException("this event has already been in this collection");
        eventCompilationRepository.save(new EventCompilation(eventId, compId));
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        checkCompId(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        if (compilation.isPinned())
            throw new UpdateException("compilation has already been pinned");
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void unpinCompilation(Long compId) {
        checkCompId(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        if (!compilation.isPinned())
            throw new UpdateException("compilation has already been unpinned");
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        checkCompId(compId);
        return compilationMapper.toCompilationDto(compilationRepository.getReferenceById(compId));
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());
        return compilationRepository.findAllByPinned(pinned, pageRequest).stream()
                .map(compilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    private void checkCompId(Long compId) {
        if (!compilationRepository.existsById(compId))
            throw new IncorrectParameterException("bad compilation id");
    }
}
