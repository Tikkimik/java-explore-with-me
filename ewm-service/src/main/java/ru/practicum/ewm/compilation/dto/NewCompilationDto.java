package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {

    @NotNull
    private List<Long> events;

    @NotNull
    private boolean pinned;

    @NonNull
    @NotBlank
    private String title;

}