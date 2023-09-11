package ru.practicum.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {

    Long id;
    String name;
}
