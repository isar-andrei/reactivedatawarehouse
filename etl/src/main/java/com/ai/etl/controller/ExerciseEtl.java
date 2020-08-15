package com.ai.etl.controller;

import com.ai.etl.domain.Exercise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ExerciseEtl extends AbstractEtl {

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
