package ru.practicum.ewm.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.mapper.CommentMapper;
import ru.practicum.ewm.comment.model.Comment;
import ru.practicum.ewm.comment.repository.CommentRepository;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exceptions.IncorrectParameterException;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId, int from, int size) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Event not found dy eventId.");

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("created").descending());

        return commentRepository.getAllByEventId(eventId, pageRequest).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByEventIdAndCommentId(Long eventId, Long commId) {

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Event not found dy eventId.");

        if (!commentRepository.existsById(commId))
            throw new IncorrectParameterException("Comment not found dy commId.");

        Comment comment = commentRepository.getReferenceById(commId);

        if (!Objects.equals(comment.getEventId(), eventId))
            throw new IncorrectParameterException("Event doesn't have this comment");

        return commentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("User not found dy userId.");

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Event not found dy eventId.");

        return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(newCommentDto, userId, eventId, LocalDateTime.now())));
    }

    @Override
    public CommentDto update(Long userId, Long eventId, CommentDto commentDto) {
        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("User not found dy userId.");

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Event not found dy eventId.");

        if (!Objects.equals(commentDto.getUser().getId(), userId))
            throw new IncorrectParameterException("you can't update someone else's comment");

        if (!Objects.equals(commentDto.getEvent().getId(), eventId))
            throw new IncorrectParameterException("this comment from another event");

        return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(commentDto)));
    }

    @Override
    public List<CommentDto> getAllByUser(Long userId, int from, int size) {

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("User not found dy userId.");

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("created").descending());
        return commentRepository.getAllByUserId(userId, pageRequest).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long eventId, Long commId) {

        if (!commentRepository.existsById(commId))
            throw new IncorrectParameterException("Comment not found dy commId.");

        Comment comment = commentRepository.getReferenceById(commId);

        if (!userRepository.existsById(userId))
            throw new IncorrectParameterException("User not found dy userId.");

        if (!Objects.equals(comment.getUserId(), userId))
            throw new IncorrectParameterException("you can't delete someone else's comment");

        if (!eventRepository.existsById(eventId))
            throw new IncorrectParameterException("Event not found dy eventId.");

        if (!Objects.equals(comment.getEventId(), eventId))
            throw new IncorrectParameterException("this comment from another event");

        commentRepository.deleteById(commId);
    }

    @Override
    public List<CommentDto> getAll(int from, int size) {

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("created").descending());

        return commentRepository.findAll(pageRequest).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findByText(String text, int from, int size) {

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by("created").descending());

        return commentRepository.findAllByMessageContainsIgnoreCase(text, pageRequest).stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByAdmin(Long commId) {

        if (!commentRepository.existsById(commId))
            throw new IncorrectParameterException("Comment not found dy commId.");

        commentRepository.deleteById(commId);
    }
}