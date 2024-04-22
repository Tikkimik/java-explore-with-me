package ru.practicum.ewm.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.comment.dto.CommentDto;
import ru.practicum.ewm.comment.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/comments")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getAll(@RequestParam(required = false, defaultValue = "0") int from,
                                   @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get all comments.");
        return commentService.getAll(from, size);
    }

    @GetMapping("/find")
    public List<CommentDto> findByText(@RequestParam String text,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Get comments with text-{}.", text);
        return commentService.findByText(text, from, size);
    }

    @DeleteMapping("/{commId}")
    public void delete(@PathVariable long commId) {
        log.info("Delete coment with commId={}.", commId);
        commentService.deleteByAdmin(commId);
    }
}
