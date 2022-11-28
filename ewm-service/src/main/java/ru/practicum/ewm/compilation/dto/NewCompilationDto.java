package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {

    private List<Long> events;

    private boolean pinned;

    @NonNull
    private String title;
}
