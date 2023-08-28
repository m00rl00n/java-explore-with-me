package ru.practicum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsResponse {

    String app;
    String uri;
    Integer hits;
}