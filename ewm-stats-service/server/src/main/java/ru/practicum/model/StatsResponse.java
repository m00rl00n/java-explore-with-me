package ru.practicum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(of = "id")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsResponse {
    String app;
    String uri;
    Integer hits;
}