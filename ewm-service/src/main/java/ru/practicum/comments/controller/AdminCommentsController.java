package ru.practicum.comments.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.CommentService;

@RestController
@RequestMapping("/admin/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminCommentsController {

    final CommentService commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        commentService.deleteByAdmin(id);
    }

    @PatchMapping("/{commentId}/approve")
    public CommentDto approve(@PathVariable Long id) {
        return commentService.approve(id);
    }

    @PatchMapping("/{commentId}/reject")
    public CommentDto reject(@PathVariable Long id) {
        return commentService.reject(id);
    }
}
