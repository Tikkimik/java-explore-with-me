package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class AdminUpdateEventRequest {

    private String annotation;

    private Long category;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    @NotBlank
    private String eventDate;

    @NotNull
    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    @NotNull
    private boolean requestModeration;

    @NotNull
    @NotBlank
    private String title;

}