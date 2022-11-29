package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.location.mapper.LocationMapper;
import ru.practicum.ewm.location.repository.LocationRepository;
import ru.practicum.ewm.request.repository.ParticipationRequestsRepository;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.user.dto.UserShortDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class EventMapper {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestsRepository prRepository;

    public Event toEvent(UpdateEventRequest eventDto) {
        return new Event(
                eventDto.getEventId(),
                eventDto.getAnnotation(),
                eventDto.getCategory(),
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), dtf),
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.getTitle()
        );
    }

    public Event toEvent(NewEventDto eventDto) {

        return new Event(
                eventDto.getAnnotation(),
                eventDto.getCategory(),
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), dtf),
                locationRepository.getLocationByLatAndLon(
                        eventDto.getLocation().getLat(), eventDto.getLocation().getLon()).getId(),
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.isRequestModeration(),
                eventDto.getTitle()
        );
    }

    public Event toEvent(UpdateEventRequest eventDto, long eventId) {
        return new Event(
                eventId,
                eventDto.getAnnotation(),
                eventDto.getCategory(),
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), dtf),
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.getTitle()
        );
    }

    public EventFullDto toEventFullDtoNotPublished(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                dtf.format(event.getCreatedOn()),
                event.getDescription(),
                dtf.format(event.getEventDate()),
                event.getId(),
                UserMapper.toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                LocationMapper.toLocationDto(locationRepository.getReferenceById(event.getLocation())),
                event.isPaid(),
                event.getParticipantLimit(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

//    public EventFullDto toEventFullDtoPublished(Event event) {
//        return new EventFullDto(
//                event.getAnnotation(),
//                CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
//                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
//                dtf.format(event.getCreatedOn()),
//                event.getDescription(),
//                dtf.format(event.getEventDate()),
//                event.getId(),
//                UserMapper.toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
//                LocationMapper.toLocationDto(locationRepository.getReferenceById(event.getLocation())),
//                event.isPaid(),
//                event.getParticipantLimit(),
//                dtf.format(event.getPublishedOn()),
//                event.isRequestModeration(),
//                event.getState(),
//                event.getTitle(),
//                event.getViews()
//        );
//    }
///
    public static EventFullDto mapToEventFullDtoPublished(Event event,
                                                          CategoryDto categoryDto,
                                                          Long confirmedRequests,
                                                          UserShortDto initiator,
                                                          LocationDto location) {
        return new EventFullDto(
                event.getAnnotation(),
                categoryDto,
                //CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                confirmedRequests,
                //prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                dtf.format(event.getCreatedOn()),
                event.getDescription(),
                dtf.format(event.getEventDate()),
                event.getId(),
                initiator,
//                UserMapper.toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                location,
//                LocationMapper.toLocationDto(locationRepository.getReferenceById(event.getLocation())),
                event.isPaid(),
                event.getParticipantLimit(),
                dtf.format(event.getPublishedOn()),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(categoryRepository.getReferenceById(event.getCategory())),
                prRepository.countParticipationRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED.toString()),
                dtf.format(event.getEventDate()),
                event.getId(),
                UserMapper.toUserShortDto(userRepository.getReferenceById(event.getInitiator())),
                event.isPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}
