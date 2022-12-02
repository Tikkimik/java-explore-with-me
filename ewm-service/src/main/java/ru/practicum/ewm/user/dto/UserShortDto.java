package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserShortDto {

    private Long id;

    @NotNull
    @NotBlank
    private String name;
}
