package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.EventCompilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.repository.EventCompilationRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.exceptions.UpdateException;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.ParticipationRequestsRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm.compilation.mapper.CompilationMapper.mapToCompilation;
import static ru.practicum.ewm.compilation.mapper.CompilationMapper.mapToCompilationDto;
import static ru.practicum.ewm.event.mapper.EventMapper.toEventShortDto;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserShortDto;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final ParticipationRequestsRepository prRepository;
    private final EventCompilationRepository eventCompilationRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapToCompilation(newCompilationDto);
        compilation = compilationRepository.save(compilation);
        for (Long eventId : newCompilationDto.getEvents()) {
            EventCompilation eventCompilation = new EventCompilation(eventId, compilation.getId());
            eventCompilationRepository.save(eventCompilation);
        }
        return mapToCompilationDto(compilation, eventRepository.getCompilationsEvents(compilation.getId()).stream()
                .map(event -> toEventShortDto(
                        event,
                        toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                        prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                        toUserShortDto(userRepository.getReferenceById(event.getInitiator()))
                ))
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteCompilation(Long compId) {
        checkCompId(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteCompilationEvent(Long compId, Long eventId) {
        checkCompId(compId);
        EventCompilation ec = eventCompilationRepository.getByCompilationIdAndEventId(compId, eventId);
        eventCompilationRepository.deleteById(ec.getId());
    }

    @Override
    public void createCompilationEvent(Long compId, Long eventId) {
        checkCompId(compId);
        if (eventCompilationRepository.existsByCompilationIdAndEventId(compId, eventId))
            throw new IncorrectParameterException("Event has already been in this collection.");
        eventCompilationRepository.save(new EventCompilation(eventId, compId));
    }

    @Override
    public void pinCompilation(Long compId) {
        checkCompId(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        if (compilation.isPinned())
            throw new UpdateException("Compilation has already been pinned.");
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        checkCompId(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        if (!compilation.isPinned())
            throw new UpdateException("Compilation has already been unpinned.");
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        checkCompId(compId);
        Compilation compilation = compilationRepository.getReferenceById(compId);
        return mapToCompilationDto(compilation, eventRepository.getCompilationsEvents(compilation.getId())
                .stream()
                .map(event -> toEventShortDto(
                        event,
                        toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                        prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                        toUserShortDto(userRepository.getReferenceById(event.getInitiator()))
                ))
                .collect(Collectors.toList()));
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());
        List<CompilationDto> compilationDtos = new ArrayList<>();
        Page<Compilation> allByPinned = compilationRepository.findAllByPinned(pinned, pageRequest);

        for (Compilation compilation : allByPinned) {
            compilationDtos.add(mapToCompilationDto(compilation, eventRepository.getCompilationsEvents(compilation.getId())
                    .stream()
                    .map(event -> toEventShortDto(
                            event,
                            toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                            prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                            toUserShortDto(userRepository.getReferenceById(event.getInitiator()))
                    ))
                    .collect(Collectors.toList())));
        }

        return compilationDtos;
    }

    private void checkCompId(Long compId) {
        if (!compilationRepository.existsById(compId))
            throw new IncorrectParameterException("Bad compilation id.");
    }
}