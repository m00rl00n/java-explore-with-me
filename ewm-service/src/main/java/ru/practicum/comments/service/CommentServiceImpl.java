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
        log.info("Создание нового комментария пользователем {} к событию {}", userId, eventId);
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("Пользователь с id " + userId + " не найден"));

            log.debug("Поиск подтвержденных событий пользователя с id={} и события с id={}...", userId, eventId);
            Long count = requestRepository.countByPublishedEventsAndStatuses(
                    userId,
                    eventId,
                    newCommentDto.getCreated() == null ? LocalDateTime.now() : newCommentDto.getCreated(),
                    EventState.PUBLISHED,
                    List.of("CONFIRMED", "ACCEPTED")
            );

            if (count > 0) {
                log.debug("Создание комментария из DTO для пользователя с id={} и события с id={}", userId, eventId);
                Comment comment = commentRepository.save(getCommentFromDto(user, eventId, newCommentDto));
                log.info("Комментарий сохранен с id={}", comment.getId());
                return CommentDtoMapper.toDto(comment);
            } else {
                log.warn("Пользователь с id={} не был на событии с id={}, комментарий невозможно добавить.", userId, eventId);
                throw new WrongDataException("Пользователь не был на событии, комментарий невозможно добавить.");
            }
        } catch (Exception e) {
            log.error("Ошибка при создании комментария пользователя {} к событию {}: {}", userId, eventId, e.getMessage());
            throw e;
        }
    }

    @Override
    public CommentDto update(Long userId, Long commentId, CommentDto updateCommentDto) {
        log.info("Изменение комментария {} пользователем {}", commentId, userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new NotFoundException("Пользователь не найден " + userId)
            );

            log.debug("Поиск комментария с id={}...", commentId);
            Comment newComment = getCommentFromDto(user, commentId, updateCommentDto);
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NotFoundException("Комментарий не найден " + commentId)
            );

            if (userId.equals(comment.getAuthor().getId())) {
                log.debug("Изменение текста комментария с id={}", commentId);
                comment.setText(newComment.getText());
                comment.setUpdated(LocalDateTime.now());
                comment.setState("NEW");
                commentRepository.save(comment);
                log.info("Комментарий обновлен с id={}", commentId);
                return CommentDtoMapper.toDto(comment);
            } else {
                log.warn("Пользователь с id={} не является автором комментария с id={}. Изменение невозможно", userId, commentId);
                throw new WrongDataException("Пользователь " + userId + " не является автором комментария. Изменение невозможно");
            }
        } catch (Exception e) {
            log.error("Ошибка при изменении комментария {} пользователем {}: {}", commentId, userId, e.getMessage());
            throw e;
        }
    }


    @Override
    public List<CommentDto> getByEvent(Long eventId, Integer from, Integer size) {
        log.info("Получение комментариев к событию {}", eventId);
        List<Comment> comments = commentRepository.findCommentsByEventIdAndStateOrderByCreatedDesc(eventId, "APPROVED", PageRequest.of(from / size, size));
        log.info("Найдено комментариев: " + comments.size());
        return comments.stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getByUser(Long userId, Integer from, Integer size) {
        log.info("Получение комментариев пользователя {}", userId);
        List<Comment> comments = commentRepository.findCommentsByAuthorIdOrderByCreatedDesc(userId, PageRequest.of(from / size, size));
        log.info("Найдено комментариев: " + comments.size());
        return comments.stream()
                .map(CommentDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long commentId) {
        log.info("Удаление комментария {} пользователем {}", commentId, userId);
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь не найден " + userId)
        );
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарй не найден " + commentId)
        );
        if (userId.equals(comment.getAuthor().getId())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new WrongDataException("Пользователь " + userId + " не является автором комментария. Удаление невозможно");
        }
        log.info("Комментарий удален {}", commentId);
    }

    @Override
    public void deleteByAdmin(Long commentId) {
        log.info("Удаление комментария администратором {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарй не найден " + commentId)
        );
        commentRepository.deleteById(commentId);
        log.info("Комментарий удален {}", commentId);
    }

    @Override
    public CommentDto getCommentById(Long commentId) {
        log.info("Получение комментария {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарй не найден " + commentId)
        );
        return CommentDtoMapper.toDto(comment);
    }

    @Override
    public CommentDto approve(Long commentId) {
        log.info("Утверждение комментария {}", commentId);
        try {
            log.debug("Поиск комментария с id={}...", commentId);
            Comment comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new NotFoundException("Комментарий не найден " + commentId)
            );

            log.debug("Установка состояния комментария на 'APPROVED'...");
            comment.setState("APPROVED");
            comment.setPublished(LocalDateTime.now());

            log.debug("Сохранение утвержденного комментария с id={}...", commentId);
            return CommentDtoMapper.toDto(commentRepository.save(comment));
        } catch (Exception e) {
            log.error("Ошибка при утверждении комментария {}: {}", commentId, e.getMessage());
            throw e;
        }
    }

    @Override
    public CommentDto reject(Long commentId) {
        log.info("отклонение комментария {}", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException("Комментарй не обнаружен " + commentId)
        );
        comment.setState("REJECTED");
        return CommentDtoMapper.toDto(commentRepository.save(comment));
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