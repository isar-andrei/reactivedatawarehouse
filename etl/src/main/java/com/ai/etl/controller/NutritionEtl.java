package com.ai.etl.controller;

import com.ai.etl.domain.Food;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class NutritionEtl {

    WebClient readWebClient = WebClient.create("http://localhost:8090");
    WebClient writeWebClient = WebClient.create("http://localhost:8080");

    private final static MediaType json = MediaType.APPLICATION_JSON;

    @PostMapping("/etl/nutritions")
    public Flux<Food> insert() {
        return readWebClient.get()
                .uri("/api/foods")
                .retrieve()
                .bodyToFlux(Food.class)
                .flatMap(food -> writeWebClient.post()
                        .uri("/api/nutritions")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"uuid\":\"" + food.getId() + "\"," +
                                                      "\"name\":\"" + food.getName() + "\"," +
                                                      "\"calories\":\"" + food.getCalories() + "\"," +
                                                      "\"fat\":\"" + food.getFat() + "\"," +
                                                      "\"saturatedFat\":\"" + food.getSaturatedFat() + "\"," +
                                                      "\"carbohydrates\":\"" + food.getCarbohydrates() + "\"," +
                                                      "\"fiber\":\"" + food.getFiber() + "\"," +
                                                      "\"sugar\":\"" + food.getSugar() + "\"," +
                                                      "\"protein\":\"" + food.getProtein() + "\"," +
                                                      "\"sodium\":\"" + food.getSodium() + "\"" +
                                                      "}"))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> clientHttpResponse.getBody());
                            }
                            return clientResponse.bodyToMono(Food.class);
                        })
                );
    }
}
