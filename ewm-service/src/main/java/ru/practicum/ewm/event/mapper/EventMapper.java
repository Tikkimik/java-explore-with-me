package ru.practicum.ewm.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.location.model.Location;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewm.category.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.ewm.location.mapper.LocationMapper.toLocationDto;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserShortDto;

@RequiredArgsConstructor
@Component
public class EventMapper {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event mapNewEventDtoToEvent(NewEventDto eventDto,
                                              Category category,
                                              Location location,
                                              User initiator) {
        return new Event(
                eventDto.getAnnotation(),
                category,
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), dtf),
                location,
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.isRequestModeration(),
                eventDto.getTitle(),
                LocalDateTime.now(),
                EventState.PENDING.toString(),
                0L,
                initiator
        );
/*  ====================================================================================================================
        --=NewEventDto=--                             --=EVENT=--
        String annotation;                        String annotation;
        Long category;                       =--> Category category;
        String description;                       String description;
        String eventDate;                    =--> LocalDateTime eventDate;
        LocationDto location;                =--> Location location1;
        boolean paid = false;                     boolean paid;
        Long participantLimit = 0L;               Long participantLimit;
        boolean requestModeration = true;         boolean requestModeration;
        String title;                             String title;
                                                  LocalDateTime createdOn;
                                                  String state;
                                                  Long views;
                                                  User initiator;

                                                  --=ADD IN METHOD=--
                                                  Long id;
                                                  long confirmedRequests;

                                                    --=NOT USED=--
                                                  LocalDateTime publishedOn;
    ==================================================================================================================*/
    }

    public static EventFullDto mapToEventFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                dtf.format(event.getCreatedOn()),
                event.getDescription(),
                dtf.format(event.getEventDate()),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                toLocationDto(event.getLocation()),
                event.isPaid(),
                event.getParticipantLimit(),
                null,
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
/*  ====================================================================================================================
         --= EventFullDto =--        <--      --=event=--
         String annotation;          ->     String annotation;
         CategoryDto category;       ->>>>  Category category;
         Long confirmedRequests;     ->     long confirmedRequests;
         String createdOn;           ->>>>  LocalDateTime createdOn;
         String description;         ->     String description;
         String eventDate;           ->>>>  LocalDateTime eventDate;
         Long id;                    ->     Long id;
         UserShortDto initiator;     ->>    User initiator;
         LocationDto location;       ->>>>  Location location;
         boolean paid;               ->     boolean paid;
         Long participantLimit;      ->     Long participantLimit;
         String publishedOn;         ->>>>  LocalDateTime publishedOn;--
         boolean requestModeration;  ->     boolean requestModeration;
         String state;               ->     String state;
         String title;               ->     String title;
         Long views;                 ->     Long views;
    ==================================================================================================================*/
    }

    public static EventFullDto mapToEventFullDtoPublished(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                dtf.format(event.getCreatedOn()),
                event.getDescription(),
                dtf.format(event.getEventDate()),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                toLocationDto(event.getLocation()),
                event.isPaid(),
                event.getParticipantLimit(),
                dtf.format(event.getPublishedOn()),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
/*  ====================================================================================================================
         --= EventFullDto =--        <--      --=event=--
         String annotation;          ->     String annotation;
         CategoryDto category;       ->>>>  Category category;
         Long confirmedRequests;     ->     long confirmedRequests;
         String createdOn;           ->>>>  LocalDateTime createdOn;
         String description;         ->     String description;
         String eventDate;           ->>>>  LocalDateTime eventDate;
         Long id;                    ->     Long id;
         UserShortDto initiator;     ->>    User initiator;
         LocationDto location;       ->>>>  Location location;
         boolean paid;               ->     boolean paid;
         Long participantLimit;      ->     Long participantLimit;
         String publishedOn;         ->>>>  LocalDateTime publishedOn;++
         boolean requestModeration;  ->     boolean requestModeration;
         String state;               ->     String state;
         String title;               ->     String title;
         Long views;                 ->     Long views;
    ==================================================================================================================*/
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                dtf.format(event.getEventDate()),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews()
        );
/*  ====================================================================================================================
       --=EventShortDto=--                  <-             --=EVENT=--
    String annotation;                                  String annotation;
    CategoryDto category;                               Category category;
    String eventDate;                                   LocalDateTime eventDate;
    boolean paid;                                       boolean paid;
    String title;                                       String title;
    Long views;                                         Long views;
    UserShortDto initiator;                             User initiator;
    Long id;                                            Long id;
    Long confirmedRequests;                             long confirmedRequests;
                                                           --=NOT USED=--
                                                        String description;
                                                        Location location1;
                                                        Long participantLimit;
                                                        boolean requestModeration;
                                                        LocalDateTime createdOn;
                                                        String state;
                                                        LocalDateTime publishedOn;

                                                         --=ADD IN METHOD=--
    ==================================================================================================================*/
    }

    public static Event mapUpdateEventRequestToEvent(UpdateEventRequest eventDto, Category category) {
        return new Event(
                eventDto.getAnnotation(),
                category,
                eventDto.getDescription(),
                LocalDateTime.parse(eventDto.getEventDate(), dtf),
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.getTitle()
        );
    }
/*  ====================================================================================================================
    --==UpdateEventRequest==--              =-->                --=EVENT=--
        String annotation;                                  String annotation;
        Long category;                                      Category category;
        String description;                                 String description;
        String eventDate;                                   LocalDateTime eventDate;
        Long eventId;                                       Long id;
        boolean paid;                                       boolean paid;
        Long participantLimit;                              Long participantLimit;
        String title;                                       String title;
    ==================================================================================================================*/
}