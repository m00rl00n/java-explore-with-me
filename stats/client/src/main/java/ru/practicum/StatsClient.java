package ru.practicum;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsClient {

    final WebClient webClient;

    public StatsClient(@Value("${server.url}") String connectionURL) {
        webClient = WebClient.create(connectionURL);
    }

    public EndpointHitDto saveHit(EndpointHitDto HitDto) {
        return webClient.post()
                .uri("/hit")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(HitDto), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .block();
    }

    public List<StatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        return List.of(webClient.get()
                .uri(uriWithParams -> uriWithParams.path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(StatsDto[].class)
                .block());
    }
}