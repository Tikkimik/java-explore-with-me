package ru.practicum.ewm.comment.service;

import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;

import java.util.List;

public interface CommentService {

    //PublicCommentController
    List<CommentDto> getCommentsByEventId(Long eventId, int from, int size);

    CommentDto getCommentByEventIdAndCommentId(Long eventId, Long commId);

    //PrivateCommentController
    CommentDto addNewComment(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, Long eventId, CommentDto commentDto);

    List<CommentDto> getAllByUser(Long userId, int from, int size);

    void delete(Long userId, Long eventId, Long commId);

    //AdminCommentController
    List<CommentDto> getAll(int from, int size);

    List<CommentDto> findByText(String text, int from, int size);

    void deleteByAdmin(Long commId);

}
