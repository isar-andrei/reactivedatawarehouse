package com.ai.etl.controller;

import com.ai.etl.domain.ExercisePerformed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

//@Slf4j
@RestController
public class ActivityEtl extends AbstractEtl {

    @PostMapping("/etl/activities")
    public Flux<ExercisePerformed> insert(
            @RequestParam(value = "starting") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate starting,
            @RequestParam(value = "ending", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ending
    ) {
        return readWebClient.get()
                .uri("/api/exercises/tracker")
                .retrieve()
                .bodyToFlux(ExercisePerformed.class)
                .filter(exercisePerformed -> exercisePerformed.getDate().toLocalDate().isAfter(starting))
                .filter(exercisePerformed -> ending.map(exercisePerformed.getDate().toLocalDate()::isBefore)
                        .orElse(true))
                .flatMap(exercisePerformed -> writeWebClient.post()
                        .uri("/api/activities")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"exercise\":{" +
                                                      "\"compcode\":\"" + exercisePerformed.getExercise().getCode() + "\"," +
                                                      "\"met\":" + exercisePerformed.getExercise().getMet() + "," +
                                                      "\"category\":\"" + exercisePerformed
                                                              .getExercise().getCategory() + "\"," +
                                                      "\"description\":\"" + exercisePerformed
                                                              .getExercise().getDescription() + "\"" +
                                                      "}," +
                                                      "\"user\":{" +
                                                      "\"username\":\"" + exercisePerformed.getUser().getUsername() + "\"," +
                                                      "\"firstName\":\"" + exercisePerformed.getUser().getFirstname() + "\"," +
                                                      "\"lastName\":\"" + exercisePerformed.getUser().getLastname() + "\"," +
                                                      "\"weight\":" + exercisePerformed.getUser().getWeight() + "," +
                                                      "\"height\":" + exercisePerformed.getUser().getHeight() + "," +
                                                      "\"gender\":\"" + exercisePerformed.getUser().getGender() + "\"," +
                                                      "\"birthday\":\"" + exercisePerformed.getUser().getBirthday().toLocalDate().toString() + "\"," +
                                                      "\"email\":\"" + exercisePerformed.getUser().getEmail() + "\"" +
                                                      "}," +
                                                      "\"duration\":" + exercisePerformed.getTime() + "," +
                                                      "\"createdAt\":\"" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(
                                exercisePerformed.getDate()) + "\"" +
                                                      "}"))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> clientHttpResponse
                                        .getBody());
                            }
                            return clientResponse.bodyToMono(ExercisePerformed.class);
                        })
                );
    }
}
