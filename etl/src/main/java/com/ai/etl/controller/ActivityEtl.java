package com.ai.etl.controller;

import com.ai.etl.domain.Activity;
import com.ai.etl.domain.ExercisePerformed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@Slf4j
public class ActivityEtl extends AbstractEtl {

    @PostMapping("/etl/activities")
    public Flux<Activity> insert(
            @RequestParam(value = "starting") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate starting,
            @RequestParam(value = "ending", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ending
    ) {
        Flux<ExercisePerformed> exercisePerformedFlux = readWebClient.get()
                .uri("/api/exercises/tracker")
                .retrieve()
                .bodyToFlux(ExercisePerformed.class)
                .filter(exercisePerformed -> exercisePerformed.getDate().toLocalDate().isAfter(starting))
                .filter(exercisePerformed -> ending.map(exercisePerformed.getDate().toLocalDate()::isBefore)
                        .orElse(true));

        Flux<Integer> userKeyFlux = exercisePerformedFlux.flatMap(exercisePerformed -> writeWebClient.get()
                .uri("/api/users/convertUUIDtoID/" + exercisePerformed.getUser().getId())
                .retrieve()
                .bodyToMono(Integer.class));

        Flux<Integer> exerciseKeyFlux = exercisePerformedFlux.flatMap(exercisePerformed -> writeWebClient.get()
                .uri("/api/exercises/convertUUIDtoID/" + exercisePerformed.getExercise().getId())
                .retrieve()
                .bodyToMono(Integer.class));

        return Flux.zip(exercisePerformedFlux, userKeyFlux, exerciseKeyFlux)
                .flatMap(tuple ->
                                 writeWebClient.post()
                                         .uri("/api/activities")
                                         .contentType(json)
                                         .body(BodyInserters.fromValue("{" +
                                                                       "\"exercise\":{" +
                                                                       "\"id\":" + tuple.getT3() +
                                                                       "}," +
                                                                       "\"user\":{" +
                                                                       "\"id\":" + tuple.getT2() +
                                                                       "}," +
                                                                       "\"uuid\":\"" + tuple.getT1().getId() + "\"," +
                                                                       "\"duration\":" + tuple.getT1().getTime() + "," +
                                                                       "\"createdAt\":\"" +
                                                                       DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(tuple.getT1().getDate()) + "\"" +
                                                                       "}"))
                                         .exchange()
                                         .flatMap(clientResponse -> {
                                             if (clientResponse.statusCode().is5xxServerError()) {
                                                 clientResponse.body((clientHttpResponse, context) -> clientHttpResponse.getBody());
                                             }
                                             return clientResponse.bodyToMono(Activity.class);
                                         }));
    }
}
