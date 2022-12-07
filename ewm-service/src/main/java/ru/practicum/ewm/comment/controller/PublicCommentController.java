package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments/{eventId}")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getCommentsByEvent(@PathVariable Long eventId,
                                          @RequestParam(required = false, defaultValue = "0") int from,
                                          @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get comments for event with eventId={}.", eventId);
        return commentService.getCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/{commId}")
    public CommentDto getCommentByEventIdAndCommentId(@PathVariable Long eventId,
                          @PathVariable Long commId) {
        log.info("Get comment for event with eventId={}, commId={}.", eventId, commId);
        return commentService.getCommentByEventIdAndCommentId(eventId, commId);
    }
}
