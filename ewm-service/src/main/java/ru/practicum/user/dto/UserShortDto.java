package ru.practicum.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserShortDto {

    Long id;
    String name;
}
