package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {

    private Long id;

    @NotNull
    private List<EventShortDto> events;

    private boolean pinned;

    @NotNull
    @NotBlank
    private String title;

}