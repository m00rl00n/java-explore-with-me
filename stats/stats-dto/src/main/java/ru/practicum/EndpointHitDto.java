package ru.practicum;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointHitDto {

    @NotBlank
    String app;
    @NotBlank
    String uri;
    String ip;
    String timestamp;
}
