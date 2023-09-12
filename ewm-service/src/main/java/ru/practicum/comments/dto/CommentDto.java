package ru.practicum.comments.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class CommentDto {

    Long id;
    UserShortDto author;
    EventShortDto event;
    @NotBlank
    @Size(min = 5, max = 5000)
    String text;
    LocalDateTime created;
    String state;
    LocalDateTime published;
    LocalDateTime updated;
}