package ru.practicum.comments.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "text", nullable = false, length = 7000)
    String text;

    @ManyToOne
    Event event;

    @ManyToOne
    User author;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime created;

    String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime published;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updated;
}