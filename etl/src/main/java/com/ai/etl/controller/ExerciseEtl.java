package com.ai.etl.controller;

import com.ai.etl.domain.Exercise;
import com.ai.etl.domain.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ExerciseEtl {

    WebClient readWebClient = WebClient.create("http://localhost:8090");
    WebClient writeWebClient = WebClient.create("http://localhost:8080");

    private final static MediaType json = MediaType.APPLICATION_JSON;

    @PostMapping("/etl/exercises")
    public Flux<Exercise> insert() {
        return readWebClient.get()
                .uri("/api/exercises")
                .retrieve()
                .bodyToFlux(Exercise.class)
                .flatMap(exercise -> writeWebClient.post()
                        .uri("/api/exercises")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"uuid\":\"" + exercise.getId() + "\"," +
                                                      "\"compcode\":\"" + exercise.getCode() + "\"," +
                                                      "\"met\":\"" + exercise.getMet() + "\"," +
                                                      "\"category\":\"" + exercise.getCategory() + "\"," +
                                                      "\"description\":\"" + exercise.getDescription() + "\"" +
                                                      "}"))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> clientHttpResponse.getBody());
                            }
                            return clientResponse.bodyToMono(Exercise.class);
                        })
                );
    }
}
