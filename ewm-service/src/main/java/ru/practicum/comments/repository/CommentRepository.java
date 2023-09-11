package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.comments.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.event.id = :eventId AND c.state = :state ORDER BY c.created DESC")
    List<Comment> findCommentsByEventIdAndStateOrderByCreatedDesc(@Param("eventId") Long eventId, @Param("state") String state, Pageable page);
    @Query("SELECT c FROM Comment c WHERE c.author.id = :userId ORDER BY c.created DESC")
    List<Comment> findCommentsByAuthorIdOrderByCreatedDesc(@Param("userId") Long userId, Pageable page);
}