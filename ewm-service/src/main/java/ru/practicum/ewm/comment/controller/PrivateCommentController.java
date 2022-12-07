package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.dto.NewCommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}")
@RequiredArgsConstructor
public class PrivateCommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}/comments")
    public CommentDto add(@PathVariable Long userId, @PathVariable Long eventId,
                          @RequestBody NewCommentDto newCommentDto) {
        log.info("Post new comment with userId={}, eventId={}.", userId, eventId);
        return commentService.addNewComment(userId, eventId, newCommentDto);
    }

    @PatchMapping("/events/{eventId}/comments")
    public CommentDto update(@PathVariable Long userId, @PathVariable Long eventId,
                             @RequestBody CommentDto commentDto) {
        log.info("Update user comment with userId={}, eventId={}.", userId, eventId);
        return commentService.update(userId, eventId, commentDto);
    }

    @GetMapping("/comments")
    public List<CommentDto> getAllByUser(@PathVariable Long userId,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get user comments with userId={}.", userId);
        return commentService.getAllByUser(userId, from, size);
    }

    @DeleteMapping("/events/{eventId}/comments/{commId}")
    public void delete(@PathVariable Long userId,
                       @PathVariable Long eventId,
                       @PathVariable Long commId) {
        log.info("Delete user comment with userId={}, eventId={}, commId={}.", userId, eventId, commId);
        commentService.delete(userId, eventId, commId);
    }
}
