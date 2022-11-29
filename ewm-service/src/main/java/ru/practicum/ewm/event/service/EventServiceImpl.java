package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.dto.*;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.exceptions.UpdateException;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.ParticipationRequestsRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm.event.mapper.EventMapper.*;
import static ru.practicum.ewm.location.mapper.LocationMapper.toLocationDto;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserShortDto;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestsRepository prRepository;
    private final ParticipationRequestsRepository requestsRepository;

    @Override
    public EventFullDto getEvent(Long eventId) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Nothing to return. Wrong event id.");

        Event event = eventRepository.getReferenceById(eventId);

        EventFullDto dto = mapToEventFullDtoPublished(
                event,
                toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(event.getLocation()))
        );

        if (!event.getState().equals("PUBLISHED"))
            throw new IncorrectParameterException("Event published.");

        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        return dto;
    }

    @Override
    public List<EventFullDto> getEvents(String text, List<Long> categories, boolean paid, String rangeStart,
                                        String rangeEnd, String sort, boolean onlyAvailable, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                sort = "eventDate";
                pageRequest = PageRequest.of(from / size, size, Sort.by(sort).ascending());
            }
            if (sort.equals("VIEWS")) {
                sort = "views";
                pageRequest = PageRequest.of(from / size, size, Sort.by(sort).ascending());
            }
        }

        List<EventFullDto> list = filterByDate(
            eventRepository.getEventByStateAndCategoryInAndPaid(EventState.PUBLISHED.toString(), categories, paid, pageRequest)
                    .stream()
                    .collect(Collectors.toList()), rangeStart, rangeEnd)
            .stream()
            .filter(event -> event.getAnnotation().toLowerCase().contains(text.toLowerCase())
                    || event.getDescription().toLowerCase().contains(text.toLowerCase()))
            .map(event -> mapToEventFullDtoPublished(
                    event,
                    toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                    prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                    toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                    toLocationDto(locationRepository.getReferenceById(event.getLocation()))))
            .collect(Collectors.toList());

        if (!onlyAvailable)
            return list;

        return list.stream().filter(event -> event.getConfirmedRequests() < event.getParticipantLimit()).collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("id").ascending());

        return eventRepository.getAllByInitiator(userId, pageRequest).stream()
                .map(event -> toEventShortDto(
                        event,
                        toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                        prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                        toUserShortDto(userRepository.getReferenceById(event.getInitiator()))
                ))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createUserEvent(Long userId, NewEventDto eventDto) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));

        Event event = toEvent(eventDto, locationRepository.getLocationByLatAndLon(
                eventDto.getLocation().getLat(), eventDto.getLocation().getLon()).getId()
        );

        if (!event.getEventDate().isAfter(LocalDateTime.now().plusHours(2)))
            throw new IncorrectParameterException("Next event can start in at least 2 hours");

        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userId);
        event.setState(EventState.PENDING.toString());
        event.setViews(0L);
        return mapToEventFullDtoNotPublished(
                eventRepository.save(event),
                toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(event.getLocation()))
        );
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, UpdateEventRequest eventDto) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");


        if (!eventRepository.existsById(eventDto.getEventId()))
            throw new IncorrectParameterException("Wrong event id (eventId)");

        Event eventNew = toEvent(eventDto);
        Event event = eventRepository.getReferenceById(eventDto.getEventId());

        if (event.getState().equals("PUBLISHED"))
            throw new UpdateException("Event published alrd");


        if (!eventNew.getEventDate().isAfter(LocalDateTime.now().plusHours(2)))
            throw new IncorrectParameterException("Next event can start in at least 2 hours");

        eventNew.setLocation(event.getLocation());
        eventNew.setCreatedOn(event.getCreatedOn());
        eventNew.setInitiator(userId);
        eventNew.setState(event.getState());
        eventNew.setViews(event.getViews());
        eventRepository.save(eventNew);
        return mapToEventFullDtoNotPublished(
                eventRepository.save(eventNew),
                toCategoryDto(categoryRepository.getReferenceById(eventNew.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(eventNew.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(eventNew.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(eventNew.getLocation()))
        );
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId)");

        Event event = eventRepository.getReferenceById(eventId);

        if (event.getState().equals("PUBLISHED")) {
            return mapToEventFullDtoPublished(
                    event,
                    toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                    prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                    toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                    toLocationDto(locationRepository.getReferenceById(event.getLocation()))
            );
        } else
            return mapToEventFullDtoNotPublished(
                    eventRepository.save(event),
                    toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                    prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                    toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                    toLocationDto(locationRepository.getReferenceById(event.getLocation()))
            );
    }

    @Override
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId)");

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        Event event = eventRepository.getReferenceById(eventId);
        if (!Objects.equals(event.getInitiator(), userId))
            throw new IncorrectParameterException("you don't have event with this id");

        if (event.getState().equals("PUBLISHED"))
            throw new UpdateException("Event published");

        event.setState(EventState.CANCELED.toString());
        eventRepository.save(event);
        return mapToEventFullDtoNotPublished(
                eventRepository.save(event),
                toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(event.getLocation()))
        );
    }

    @Override
    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId)");

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        return requestsRepository.getAllByEventId(eventId).stream()
                .map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmEventRequest(Long userId, Long eventId, Long reqId) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        if (!requestsRepository.existsById(reqId))
            throw new IncorrectParameterException("Wrong request id (requestId).");

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId).");

        Event event = eventRepository.getReferenceById(eventId);
        if (Objects.equals(event.getParticipantLimit(), requestsRepository.countParticipationRequestByEventIdAndStatus(eventId, RequestStatus.CONFIRMED.toString())))
            throw new UpdateException("all seats are occupied.");

        ParticipationRequest request = requestsRepository.getReferenceById(reqId);

        if (request.getStatus().equals(RequestStatus.CONFIRMED.toString()))
            throw new UpdateException("Request already confirmed.");

        request.setStatus(RequestStatus.CONFIRMED.toString());
        requestsRepository.save(request);

        if (Objects.equals(event.getParticipantLimit(), requestsRepository.countParticipationRequestByEventIdAndStatus(eventId, RequestStatus.CONFIRMED.toString())))
            requestsRepository.getAllByStatusAndEventId(RequestStatus.PENDING.toString(), eventId)
                    .forEach(req -> rejectEventRequest(userId, eventId, req.getId()));

        return RequestMapper.toDto(request);
    }

    @Override
    public RequestDto rejectEventRequest(Long userId, Long eventId, Long reqId) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId).");

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        if (!requestsRepository.existsById(reqId))
            throw new IncorrectParameterException("Wrong request id (requestId).");

        ParticipationRequest request = requestsRepository.getReferenceById(reqId);
        request.setStatus(RequestStatus.REJECTED.toString());
        requestsRepository.save(request);
        return RequestMapper.toDto(request);
    }

    @Override
    public List<EventFullDto> adminSearchEvents(List<Long> users, List<String> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);

        if (states == null)
            return filterByDate(eventRepository.getEventsByCategoryInAndInitiatorIn(categories, users, pageRequest).toList(), rangeStart, rangeEnd)
                    .stream()
                    .map(event -> mapToEventFullDtoPublished(
                            event,
                            toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                            prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                            toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                            toLocationDto(locationRepository.getReferenceById(event.getLocation()))))
                    .collect(Collectors.toList());

        return filterByDate(eventRepository.getEventsByStateInAndCategoryInAndInitiatorIn(
                states, categories, users, pageRequest).toList(), rangeStart, rangeEnd)
                .stream()
                .map(event -> mapToEventFullDtoPublished(
                        event,
                        toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                        prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                        toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                        toLocationDto(locationRepository.getReferenceById(event.getLocation()))))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequest eventDto) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId).");

        Event eventNew = toEvent(eventDto, eventId);
        Event event = eventRepository.getReferenceById(eventId);
        eventNew.setRequestModeration(event.isRequestModeration());
        eventNew.setLocation(event.getLocation());
        eventNew.setCreatedOn(event.getCreatedOn());
        eventNew.setInitiator(event.getInitiator());
        eventNew.setState(event.getState());
        eventNew.setViews(event.getViews());
        if (eventNew.getState().equals("PUBLISHED")) {
            eventNew.setPublishedOn(event.getPublishedOn());
            eventRepository.save(eventNew);
            return mapToEventFullDtoPublished(
                    eventNew,
                    toCategoryDto(categoryRepository.getReferenceById(eventNew.getCategory())),
                    prRepository.countParticipationRequestByEventIdAndStatus(eventNew.getId(), RequestStatus.CONFIRMED.toString()),
                    toUserShortDto(userRepository.getReferenceById(eventNew.getInitiator())),
                    toLocationDto(locationRepository.getReferenceById(eventNew.getLocation()))
            );
        }
        eventRepository.save(eventNew);
        return mapToEventFullDtoNotPublished(
                eventRepository.save(eventNew),
                toCategoryDto(categoryRepository.getReferenceById(eventNew.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(eventNew.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(eventNew.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(eventNew.getLocation()))
        );
    }

    @Override
    public EventFullDto adminPublishEvent(Long eventId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getState().equals(EventState.PENDING.toString()))
            throw new UpdateException("Event is not pending.");
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new UpdateException("Event will start in less than an hour.");
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED.toString());
        eventRepository.save(event);
        return mapToEventFullDtoPublished(
                event,
                toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(event.getLocation()))
        );
    }

    @Override
    public EventFullDto adminRejectEvent(Long eventId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getState().equals(EventState.PENDING.toString()))
            throw new UpdateException("Event is not pending.");
        event.setState(EventState.CANCELED.toString());
        eventRepository.save(event);
        return mapToEventFullDtoNotPublished(
                eventRepository.save(event),
                toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                toLocationDto(locationRepository.getReferenceById(event.getLocation()))
        );
    }

    private List<Event> filterByDate(List<Event> events, String start, String end) {
        if (start == null && end == null)
            return events.stream()
                    .filter(event -> event.getEventDate().isAfter(LocalDateTime.now()))
                    .collect(Collectors.toList());

        LocalDateTime starts = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime ends = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return events.stream()
                .filter(event -> event.getEventDate().isAfter(starts))
                .filter(event -> event.getEventDate().isBefore(ends))
                .collect(Collectors.toList());
    }
}