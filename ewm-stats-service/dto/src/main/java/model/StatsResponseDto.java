package model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponseDto {
    private String app = "ewm-main-service";

    private String uri;

    private long hits;
}