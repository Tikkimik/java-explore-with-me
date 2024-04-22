package ru.practicum.ewm.comment.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;

import static ru.practicum.ewm.event.mapper.EventMapper.toEventShortDto;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserShortDto;

@RequiredArgsConstructor
@Component
public class CommentMapper {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getMessage(),
                toEventShortDto(eventRepository.getReferenceById(comment.getEventId())),
                toUserShortDto(userRepository.getReferenceById(comment.getUserId())),
                comment.getCreated()
        );
    }

    public Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getMessage(),
                commentDto.getEvent().getId(),
                commentDto.getUser().getId(),
                commentDto.getCreated()
        );
    }

    public Comment toComment(NewCommentDto newCommentDto, Long userId, Long eventId, LocalDateTime created) {
        return new Comment(
                newCommentDto.getMessage(),
                eventId,
                userId,
                created
        );
    }
}
