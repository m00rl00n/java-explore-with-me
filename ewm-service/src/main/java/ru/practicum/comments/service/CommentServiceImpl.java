package ru.practicum.comments.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.WrongDataException;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {

    final EventRepository eventRepository;
    final UserRepository userRepository;
    final CommentRepository commentRepository;
    final RequestRepository requestRepository;

    @Override
    public CommentDto add(Long userId, Long eventId, CommentDto newCommentDto) {
        log.info("Попытка создания нового комментария пользователем {} к событию {}", userId, eventId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        Comment comment;
        Long count = requestRepository.countByPublishedEventsAndStatuses(
                userId,
                eventId,
                newCommentDto.getCreated() == null ? LocalDateTime.now() : newCommentDto.getCreated(),
                EventState.PUBLISHED,
                List.of("CONFIRMED", "ACCEPTED")
        );
        if (count > 0) {
            comment = commentRepository.save(getCommentFromDto(user, eventId, newCommentDto));
            log.info("Комментарий успешно сохранен с id {}", comment.getId());
        } else {
            throw new WrongDataException("Пользователь не был на событии, комментарий невозможно добавить.");
        }
        return CommentDtoMapper.toDto(comment);
    }

    @Override
    public List<CommentDto> getByEvent(Long eventId, Integer from, Integer size) {
        log.info("Попытка получения комментариев к событию {}", eventId);
        List<Comment> comments = commentRepository.findCommentsByEventIdAndStateOrderByCreatedDesc(eventId, "APPROVED", PageRequest.of(from / size, size));
        log.info("Найдено комментариев: {}", comments.size());
        return comments.stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getByUser(Long userId, Integer from, Integer size) {
        log.info("Попытка получения комментариев пользователя {}", userId);
        List<Comment> comments = commentRepository.findCommentsByAuthorIdOrderByCreatedDesc(userId, PageRequest.of(from / size, size));
        log.info("Найдено комментариев: {}", comments.size());
        return comments.stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto update(Long userId, Long commentId, CommentDto updateCommentDto) {
        log.info("Попытка изменения комментария {} пользователем {}", commentId, userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден " + userId)
        );
        Comment newComment = getCommentFromDto(user, commentId, updateCommentDto);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден " + commentId)
        );
        if (userId.equals(comment.getAuthor().getId())) {
            comment.setText(newComment.getText());
            comment.setUpdated(LocalDateTime.now());
            comment.setState("NEW");
            commentRepository.save(comment);
            log.info("Комментарий успешно обновлен с id {}", commentId);
        } else {
            throw new WrongDataException("Пользователь " + userId + " не является автором комментария. Изменение невозможно");
        }
        return CommentDtoMapper.toDto(comment);
    }

    @Override
    public void delete(Long userId, Long commentId) {
        log.info("Попытка удаления комментария {} пользователем {}", commentId, userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден " + userId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден " + commentId)
        );
        if (userId.equals(comment.getAuthor().getId())) {
            commentRepository.deleteById(commentId);
            log.info("Комментарий с id {} успешно удален", commentId);
        } else {
            throw new WrongDataException("Пользователь " + userId + " не является автором комментария. Удаление невозможно");
        }
    }

    @Override
    public void deleteByAdmin(Long commentId) {
        log.info("Попытка удаления комментария администратором {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден " + commentId)
        );
        commentRepository.deleteById(commentId);
        log.info("Комментарий с id {} успешно удален администратором", commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        log.info("Попытка получения комментария с id {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден " + commentId)
        );
        return CommentDtoMapper.toDto(comment);
    }

    @Override
    public CommentDto approve(Long commentId) {
        log.info("Попытка утверждения комментария с id {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не найден " + commentId)
        );
        comment.setState("APPROVED");
        comment.setPublished(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("Комментарий с id {} успешно утвержден", commentId);
        return CommentDtoMapper.toDto(comment);
    }

    @Override
    public CommentDto reject(Long commentId) {
        log.info("Попытка отклонения комментария с id {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарий не обнаружен " + commentId)
        );
        comment.setState("REJECTED");
        commentRepository.save(comment);
        log.info("Комментарий с id {} успешно отклонен", commentId);
        return CommentDtoMapper.toDto(comment);
    }

    Comment getCommentFromDto(User user, Long eventId, CommentDto commentDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Эвент не найден  " + eventId)
        );
        Comment comment = CommentDtoMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        return comment;
    }

}