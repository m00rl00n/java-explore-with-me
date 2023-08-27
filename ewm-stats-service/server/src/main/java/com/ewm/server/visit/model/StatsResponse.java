package com.ewm.server.visit.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatsResponse {
    private String app;

    @NotBlank
    private String uri;

    private long hits;
}
