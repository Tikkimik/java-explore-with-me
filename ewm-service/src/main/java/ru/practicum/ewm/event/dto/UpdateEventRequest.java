package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UpdateEventRequest {

    @Size(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;

    @NotNull
    private Long eventId;

    private boolean paid;

    private Long participantLimit;

    @Size(min = 3, max = 120)
    private String title;

}