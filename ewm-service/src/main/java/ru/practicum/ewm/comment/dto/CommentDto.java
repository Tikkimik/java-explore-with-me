package ru.practicum.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.user.dto.UserShortDto;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @Size(max = 280)
    private String message;

    private EventShortDto event;

    private UserShortDto user;

    private LocalDateTime created;

    public CommentDto(String message, EventShortDto event, UserShortDto user, LocalDateTime created) {
        this.message = message;
        this.event = event;
        this.user = user;
        this.created = created;
    }
}
