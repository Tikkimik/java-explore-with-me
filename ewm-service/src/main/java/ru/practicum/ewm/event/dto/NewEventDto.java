package ru.practicum.ewm.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @NotBlank
    private String eventDate;

    @NotNull
    private LocationDto location;

    private boolean paid = false;

    private Long participantLimit = 0L;

    private boolean requestModeration = true;

    @Size(min = 3, max = 120)
    private String title;

}