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
public class FoodEtl {

    WebClient readWebClient = WebClient.create("http://localhost:8090");
    WebClient writeWebClient = WebClient.create("http://localhost:8080");

    @PostMapping("/etl/foods/populate")
    public Flux<Food> insert() {
        return readWebClient.get()
                .uri("/api/foods")
                .retrieve()
                .bodyToFlux(Food.class)
                .flatMap(x -> writeWebClient.post()
                        .uri("/api/nutritions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"uuid\" : \"" + x.getId() + "\"," +
                                                      "\"name\" : \"" + x.getName() + "\"," +
                                                      "\"calories\" : \"" + x.getCalories() + "\"," +
                                                      "\"fat\" : \"" + x.getFat() + "\"," +
                                                      "\"saturatedFat\" : \"" + x.getSaturatedFat() + "\"," +
                                                      "\"carbohydrates\" : \"" + x.getCarbohydrates() + "\"," +
                                                      "\"fiber\" : \"" + x.getFiber() + "\"," +
                                                      "\"sugar\" : \"" + x.getSugar() + "\"," +
                                                      "\"protein\" : \"" + x.getProtein() + "\"," +
                                                      "\"sodium\" : \"" + x.getSodium() + "\"" +
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
