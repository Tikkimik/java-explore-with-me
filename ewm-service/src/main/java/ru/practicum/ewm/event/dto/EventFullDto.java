package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.location.dto.LocationDto;
import ru.practicum.ewm.user.dto.UserShortDto;

@Data
@AllArgsConstructor
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private LocationDto location;
    private boolean paid;
    private Long participantLimit;
    private String publishedOn;
    private boolean requestModeration;
    private String state;
    private String title;
    private Long views;

//    public EventFullDto(String annotation, CategoryDto category, Long confirmedRequests, String createdOn,
//                        String description, String eventDate, Long id, UserShortDto initiator, LocationDto location,
//                        boolean paid, Long participantLimit, boolean requestModeration, String state, String title,
//                        Long views) {
//        this.annotation = annotation;
//        this.category = category;
//        this.confirmedRequests = confirmedRequests;
//        this.createdOn = createdOn;
//        this.description = description;
//        this.eventDate = eventDate;
//        this.id = id;
//        this.initiator = initiator;
//        this.location = location;
//        this.paid = paid;
//        this.participantLimit = participantLimit;
//        this.requestModeration = requestModeration;
//        this.state = state;
//        this.title = title;
//        this.views = views;
//    }
}
