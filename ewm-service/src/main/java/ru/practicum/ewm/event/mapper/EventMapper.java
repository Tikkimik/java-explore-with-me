package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class EventMapper {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(UpdateEventRequest eventDto) {
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

    public static Event toEvent(NewEventDto eventDto, Long location) {

        return new Event(
                eventDto.getAnnotation(),
                eventDto.getCategory(),
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), dtf),
                location,
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.isRequestModeration(),
                eventDto.getTitle()
        );
    }

    public static Event toEvent(UpdateEventRequest eventDto, long eventId) {
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

    public static EventFullDto mapToEventFullDtoNotPublished(Event event,
                                                             CategoryDto categoryDto,
                                                             Long confirmedRequests,
                                                             UserShortDto initiator,
                                                             LocationDto location) {
        return new EventFullDto(
                event.getAnnotation(),
                categoryDto,
                confirmedRequests,
                dtf.format(event.getCreatedOn()),
                event.getDescription(),
                dtf.format(event.getEventDate()),
                event.getId(),
                initiator,
                location,
                event.isPaid(),
                event.getParticipantLimit(),
                null,
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventFullDto mapToEventFullDtoPublished(Event event,
                                                          CategoryDto categoryDto,
                                                          Long confirmedRequests,
                                                          UserShortDto initiator,
                                                          LocationDto location) {
        return new EventFullDto(
                event.getAnnotation(),
                categoryDto,
                confirmedRequests,
                dtf.format(event.getCreatedOn()),
                event.getDescription(),
                dtf.format(event.getEventDate()),
                event.getId(),
                initiator,
                location,
                event.isPaid(),
                event.getParticipantLimit(),
                dtf.format(event.getPublishedOn()),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventShortDto toEventShortDto(Event event,
                                         CategoryDto category,
                                         Long confirmedRequests,
                                         UserShortDto initiator) {
        return new EventShortDto(
                event.getAnnotation(),
                category,
                confirmedRequests,
                dtf.format(event.getEventDate()),
                event.getId(),
                initiator,
                event.isPaid(),
                event.getTitle(),
                event.getViews()
        );
    }
}