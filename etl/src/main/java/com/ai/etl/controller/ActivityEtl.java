package com.ai.etl.controller;

import com.ai.etl.domain.Activity;
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
        return readWebClient.get()
                .uri("/api/exercises/tracker")
                .retrieve()
                .bodyToFlux(Activity.class)
                .filter(exercisePerformed -> exercisePerformed.getDate().toLocalDate().isAfter(starting))
                .filter(exercisePerformed -> ending.map(exercisePerformed.getDate().toLocalDate()::isBefore)
                        .orElse(true))
                .flatMap(activity -> writeWebClient.post()
                        .uri("/api/activities")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"exercise\":{" +
                                                      "\"compcode\":\"" + activity.getExercise().getCode() + "\"," +
                                                      "\"met\":" + activity.getExercise().getMet() + "," +
                                                      "\"category\":\"" + activity.getExercise().getCategory() + "\"," +
                                                      "\"description\":\"" + activity.getExercise().getDescription() + "\"" +
                                                      "}," +
                                                      "\"user\":{" +
                                                      "\"username\":\"" + activity.getUser().getUsername() + "\"," +
                                                      "\"firstName\":\"" + activity.getUser().getFirstname() + "\"," +
                                                      "\"lastName\":\"" + activity.getUser().getLastname() + "\"," +
                                                      "\"weight\":" + activity.getUser().getWeight() + "," +
                                                      "\"height\":" + activity.getUser().getHeight() + "," +
                                                      "\"gender\":\"" + activity.getUser().getGender() + "\"," +
                                                      "\"birthday\":\"" + activity.getUser().getBirthday().toLocalDate().toString() + "\"," +
                                                      "\"email\":\"" + activity.getUser().getEmail() + "\"" +
                                                      "}," +
                                                      "\"duration\":" + activity.getTime() + "," +
                                                      "\"createdAt\":\"" + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(activity.getDate()) + "\"" +
                                                      "}"))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> clientHttpResponse
                                        .getBody());
                            }
                            return clientResponse.bodyToMono(Activity.class);
                        })
                );
    }
}
