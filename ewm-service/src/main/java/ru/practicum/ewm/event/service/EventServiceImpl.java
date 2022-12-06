package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.mapper.EventMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.exceptions.UpdateException;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.request.model.ParticipationRequest;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.repository.ParticipationRequestsRepository;
import ru.practicum.ewm.stats.clients.EventClient;
import ru.practicum.ewm.stats.hit.Stat;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.ofPattern;
import static ru.practicum.ewm.event.mapper.EventMapper.mapNewEventDtoToEvent;
import static ru.practicum.ewm.event.mapper.EventMapper.mapToEventFullDto;

@Slf4j
@Service
@RequiredArgsConstructor
@ToString
public class EventServiceImpl implements EventService {

    private final EventClient eventClient;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestsRepository requestsRepository;
    private static final DateTimeFormatter dtf = ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto getEvent(Long eventId, Stat[] stats, HttpServletRequest request) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new IncorrectParameterException("Wrong event id (eventId). Event not found."));

        if (!event.getState().equals("PUBLISHED"))
            throw new IncorrectParameterException("Event published.");

        for (Stat stat : stats) {
            if (event.getId().equals(stat.getId())) {
                event.setViews(stat.getHits());
                break;
            }
        }

        event.setConfirmedRequests(requestsRepository.countParticipationRequestByEventAndStatus(event, RequestStatus.CONFIRMED.toString()));
        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<EventFullDto> getEvents(String text, List<Long> categories, boolean paid, String rangeStart,
                                        String rangeEnd, String sort, boolean onlyAvailable, int from, int size, Stat[] stats, HttpServletRequest request) {
        LocalDateTime start;
        LocalDateTime end;
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, dtf);
        } else {
            start = LocalDateTime.parse(LocalDateTime.of(2000, 1, 1, 1, 1).toString());
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, dtf);
        } else {
            end = LocalDateTime.parse(LocalDateTime.of(2050, 1, 1, 1, 1).toString());
        }
        if (text != null) text = text.toLowerCase();

        List<Event> listEvents = eventRepository.findByParams(text, categories, paid, start, end,
                onlyAvailable, PageRequest.of(from, size, Sort.by("eventDate").ascending()));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!" + listEvents);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!" + listEvents.size());

        if (!listEvents.isEmpty()) {
            List<Long> listConfirmedRequests = requestsRepository.countParticipationRequestByEventInAndStatus(listEvents, RequestStatus.CONFIRMED.toString());
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!" + listConfirmedRequests.size());

//            if (listConfirmedRequests.size() == listEvents.size()) {
            for (Event event : listEvents) {
                for (Stat stat : stats) {
                    if (event.getId().equals(stat.getId())) {
                        event.setViews(stat.getHits());
                    }
                }
            }
//            } else {
//                throw new IncorrectParameterException("ListConfirmedRequests and listEvents has not equals sizes.");
//            }

            if (sort.equals("VIEWS"))
                listEvents.sort(Comparator.comparingLong(Event::getViews));
        }

        for (Stat s : stats)
            eventClient.addHit(request, s.getUri());

        return listEvents.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, Pageable pageable) {

        List<Event> list = eventRepository.findAllByInitiatorId(userId, pageable).getContent();

        if (list.isEmpty())
            throw new IncorrectParameterException("Event list is empty.");

        return list.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createUserEvent(Long userId, NewEventDto eventDto) {

        if (!(LocalDateTime.parse(eventDto.getEventDate(), dtf).isAfter(LocalDateTime.now().plusHours(2))))
            throw new IncorrectParameterException("Next event can start in at least 2 hours");

        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() -> new IncorrectParameterException("Cat :3 not found."));
        Location location = locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        User initiator = userRepository.findById(userId).orElseThrow(() -> new IncorrectParameterException("Wrong user id (userId). Initiator not found."));
        Event event = mapNewEventDtoToEvent(eventDto, category, location, initiator);
        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, UpdateEventRequest eventDto) {

        Event event = eventRepository.findById(eventDto.getEventId()).orElseThrow(() ->
                new IncorrectParameterException("Wrong user id (userId)."));

        if (!Objects.equals(event.getInitiator().getId(), userId))
            throw new IncorrectParameterException("You are not the owner of the event.");

        if (event.getState().equals("PUBLISHED"))
            throw new UpdateException("Event published alrd");

        if (!LocalDateTime.parse(eventDto.getEventDate(), dtf).isAfter(LocalDateTime.now().plusHours(2)))
            throw new IncorrectParameterException("Next event can start in at least 2 hours");

        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new IncorrectParameterException("Cat :3 not found."));

        LocalDateTime eventDate = LocalDateTime.parse(eventDto.getEventDate(), dtf);

        event.setAnnotation(eventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDto.getDescription());
        event.setEventDate(eventDate);
        event.setPaid(eventDto.isPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setTitle(eventDto.getTitle());

        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IncorrectParameterException("Event not found."));
        return mapToEventFullDto(event);
    }

    @Override
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IncorrectParameterException("Event not found."));

        if (!Objects.equals(event.getInitiator().getId(), userId))
            throw new IncorrectParameterException("you don't have event with this id");

        if (event.getState().equals("PUBLISHED"))
            throw new UpdateException("Event already published.");

        if (event.getState().equals(EventState.PENDING.toString())) {
            event.setState(EventState.CANCELED.toString());
        }
        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Wrong event id (eventId)");

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        return requestsRepository.getAllByEventId(eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto confirmEventRequest(Long userId, Long eventId, Long reqId) {
        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("Wrong user id (userId).");

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new IncorrectParameterException("Wrong event id (eventId)."));

        Long confirmedRequests = event.getConfirmedRequests();
        Long participantLimit = event.getParticipantLimit();

        if (Objects.equals(participantLimit, confirmedRequests))
            throw new UpdateException("all seats are occupied.");

        ParticipationRequest request = requestsRepository.findById(reqId).orElseThrow(() ->
                new IncorrectParameterException("Wrong request id (reqId)."));

        if (request.getStatus().equals(RequestStatus.CONFIRMED.toString()))
            throw new UpdateException("Request already confirmed.");

        request.setStatus(RequestStatus.CONFIRMED.toString());
        requestsRepository.save(request);

        if (Objects.equals(participantLimit, confirmedRequests)) {
            List<ParticipationRequest> requests = requestsRepository.findAllByEventAndStatus(event, RequestStatus.PENDING.toString());
            requests.forEach(pr -> pr.setStatus(RequestStatus.CANCELED.toString()));
        }

        eventRepository.save(event);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public RequestDto rejectEventRequest(Long userId, Long eventId, Long reqId) {

        ParticipationRequest request = requestsRepository.findById(reqId).orElseThrow(() ->
                new IncorrectParameterException("Wrong request id (requestId)."));

        request.setStatus(RequestStatus.REJECTED.toString());
        requestsRepository.save(request);
        return RequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<EventFullDto> adminSearchEvents(List<Long> users, List<String> states, List<Long> categories, String rangeStart, String rangeEnd, Pageable pageable) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) start = LocalDateTime.parse(rangeStart, dtf);
        if (rangeEnd != null) end = LocalDateTime.parse(rangeEnd, dtf);

        List<Event> result = eventRepository.findByParamsAdmin(users, states, categories, start, end, pageable).getContent();

        return result.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventRequest eventDto) {

        Event oldEvent = eventRepository.findById(eventId).orElseThrow(() ->
                new IncorrectParameterException("Wrong event id (eventId). Event not found."));

        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(() ->
                new IncorrectParameterException("Cat :3 not found."));

        Event newEvent = EventMapper.mapUpdateEventRequestToEvent(eventDto, category);

        if (!newEvent.getAnnotation().isBlank()) oldEvent.setAnnotation(newEvent.getAnnotation());
        if (newEvent.getCategory().getId() != 0) oldEvent.setCategory(newEvent.getCategory());
        if (!newEvent.getDescription().isBlank()) oldEvent.setDescription(newEvent.getDescription());
        if (newEvent.getParticipantLimit() > 0) oldEvent.setParticipantLimit(newEvent.getParticipantLimit());
        if (newEvent.getTitle() != null) oldEvent.setTitle(newEvent.getTitle());
        if (newEvent.getEventDate() != null) oldEvent.setEventDate(newEvent.getEventDate());
        oldEvent.setPaid(newEvent.isPaid());

        return mapToEventFullDto(eventRepository.save(oldEvent));
    }

    @Override
    public EventFullDto adminPublishEvent(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IncorrectParameterException("Event not found."));

        if (!event.getState().equals(EventState.PENDING.toString()))
            throw new UpdateException("Event is not pending.");

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new UpdateException("Event will start in less than an hour.");

        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED.toString());
        return mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto adminRejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IncorrectParameterException("Event not found."));

        if (!event.getState().equals(EventState.PENDING.toString()))
            throw new UpdateException("Event is not pending.");

        event.setState(EventState.CANCELED.toString());
        return mapToEventFullDto(eventRepository.save(event));
    }
}