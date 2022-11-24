package ru.practicum.ewm.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NonNull
    @Size(max = 50)
    private String name;

    @Email
    @NotNull
    @Size(max = 50)
    private String email;

}
