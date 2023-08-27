package model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsHitDto {

    @NotBlank
    String app;
    @NotBlank
    String uri;
    String ip;
    String timestamp;
}