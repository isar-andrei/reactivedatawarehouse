package com.ai.etl.controller;

import com.ai.etl.domain.Diet;
import com.ai.etl.domain.FoodTracker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@Slf4j
public class DietEtl {

    WebClient readWebClient = WebClient.create("http://localhost:8090");
    WebClient writeWebClient = WebClient.create("http://localhost:8080");

    private final static MediaType json = MediaType.APPLICATION_JSON;

    @PostMapping("/etl/diets")
    public Flux<Diet> insert(
            @RequestParam(value = "starting") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate starting,
            @RequestParam(value = "ending", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ending
    ) {
        Flux<FoodTracker> foodTrackerFlux = readWebClient.get()
                .uri("/api/foods/tracker")
                .retrieve()
                .bodyToFlux(FoodTracker.class)
                .filter(foodTracker -> foodTracker.getDate().toLocalDate().isAfter(starting))
                .filter(foodTracker -> ending.map(foodTracker.getDate().toLocalDate()::isBefore)
                            .orElse(true));

        Flux<Integer> userKeyFlux = foodTrackerFlux.flatMap(foodTracker -> writeWebClient.get()
                .uri("/api/users/convertUUIDtoID/" + foodTracker.getUser().getId())
                .retrieve()
                .bodyToMono(Integer.class));

        Flux<Integer> foodKeyFlux = foodTrackerFlux.flatMap(foodTracker -> writeWebClient.get()
                .uri("/api/nutritions/convertUUIDtoID/" + foodTracker.getFood().getId())
                .retrieve()
                .bodyToMono(Integer.class));

        return Flux.zip(foodTrackerFlux, userKeyFlux, foodKeyFlux)
                .flatMap(tuple ->
                                 writeWebClient.post()
                                         .uri("/api/diets")
                                         .contentType(json)
                                         .body(BodyInserters.fromValue("{" +
                                                                       "\"nutrition\":{" +
                                                                       "\"id\":" + tuple.getT3() +
                                                                       "}," +
                                                                       "\"user\":{" +
                                                                       "\"id\":" + tuple.getT2() +
                                                                       "}," +
                                                                       "\"uuid\":\"" + tuple.getT1().getId() + "\"," +
                                                                       "\"servingQuantity\":" + tuple.getT1().getServingQty() + "," +
                                                                       "\"createdAt\":\"" +
                                                                       DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(tuple.getT1().getDate()) + "\"" +
                                                                       "}"))
                                         .exchange()
                                         .flatMap(clientResponse -> {
                                             if (clientResponse.statusCode().is5xxServerError()) {
                                                 clientResponse.body((clientHttpResponse, context) -> clientHttpResponse
                                                         .getBody());
                                             }
                                             return clientResponse.bodyToMono(Diet.class);
                                         })
                );
    }
}
