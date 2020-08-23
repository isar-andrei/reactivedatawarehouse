package com.ai.etl.controller;

import com.ai.etl.domain.FoodEaten;
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
public class DietEtl extends AbstractEtl {

    @PostMapping("/etl/diets")
    public Flux<FoodEaten> insert(
            @RequestParam(value = "starting") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate starting,
            @RequestParam(value = "ending", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> ending
    ) {
        return readWebClient.get()
                .uri("/api/foods/tracker")
                .retrieve()
                .bodyToFlux(FoodEaten.class)
                .filter(foodEaten -> foodEaten.getDate().toLocalDate().isAfter(starting))
                .filter(foodEaten -> ending.map(foodEaten.getDate().toLocalDate()::isBefore)
                            .orElse(true))
                .flatMap(foodEaten -> writeWebClient.post()
                        .uri("/api/diets")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"nutrition\":{" +
                                                        "\"name\":\"" + foodEaten.getFood().getName() + "\"," +
                                                        "\"calories\":" + foodEaten.getFood().getCalories() + "," +
                                                        "\"fat\":" + foodEaten.getFood().getFat() + "," +
                                                        "\"saturatedFat\":" + foodEaten.getFood().getSaturatedFat() + "," +
                                                        "\"carbohydrates\":" + foodEaten.getFood().getCarbohydrates() + "," +
                                                        "\"fiber\":" + foodEaten.getFood().getFiber() + "," +
                                                        "\"sugar\":" + foodEaten.getFood().getSugar() + "," +
                                                        "\"protein\":" + foodEaten.getFood().getProtein() + "," +
                                                        "\"sodium\":" + foodEaten.getFood().getSodium() + "" +
                                                      "}," +
                                                        "\"user\":{" +
                                                        "\"username\":\"" + foodEaten.getUser().getUsername() + "\"," +
                                                        "\"firstName\":\"" + foodEaten.getUser().getFirstname() + "\"," +
                                                        "\"lastName\":\"" + foodEaten.getUser().getLastname() + "\"," +
                                                        "\"weight\":" + foodEaten.getUser().getWeight() + "," +
                                                        "\"height\":" + foodEaten.getUser().getHeight() + "," +
                                                        "\"gender\":\"" + foodEaten.getUser().getGender() + "\"," +
                                                        "\"birthday\":\"" + foodEaten.getUser().getBirthday().toLocalDate().toString() + "\"," +
                                                        "\"email\":\"" + foodEaten.getUser().getEmail() + "\"" +
                                                      "}," +
                                                      "\"servingQuantity\":" + foodEaten.getServingQty() + "," +
                                                      "\"createdAt\":\"" +
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(foodEaten.getDate()) + "\"" +
                                                      "}"))
                        .exchange()
                        .flatMap(clientResponse -> {
                            if (clientResponse.statusCode().is5xxServerError()) {
                                clientResponse.body((clientHttpResponse, context) -> clientHttpResponse
                                        .getBody());
                            }
                            return clientResponse.bodyToMono(FoodEaten.class);
                        })
                );
    }
}
