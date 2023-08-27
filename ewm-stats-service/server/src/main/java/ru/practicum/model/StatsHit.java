package ru.practicum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hits")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String app;

    @NotNull
    @Column(nullable = false)
    String uri;

    @Column(nullable = false)
    String ip;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "datetime", nullable = false)
    LocalDateTime timestamp;
}