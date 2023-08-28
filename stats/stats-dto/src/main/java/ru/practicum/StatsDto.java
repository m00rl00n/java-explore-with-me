package ru.practicum;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsDto {

    String app;
    String uri;
    Integer hits;
}
