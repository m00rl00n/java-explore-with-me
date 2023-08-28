package ru.practicum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Stats {

    String app;
    String uri;
    Integer hits;
}
