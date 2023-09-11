package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto add(Long userId, Long eventId, CommentDto newCommentDto);

    List<CommentDto> getByEvent(Long eventId, Integer from, Integer size);

    List<CommentDto> getByUser(Long userId, Integer from, Integer size);

    CommentDto update(Long userId, Long commentId, CommentDto updateCommentDto);

    void delete(Long userId, Long commentId);

    void deleteByAdmin(Long commentId);

    CommentDto getCommentById(Long commentId);

    CommentDto approve(Long commentId);

    CommentDto reject(Long commentId);
}
