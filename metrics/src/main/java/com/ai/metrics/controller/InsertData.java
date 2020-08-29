package com.ai.metrics.controller;

import com.ai.metrics.domain.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
public class InsertData {

    private WebClient writeWebClient = WebClient.create("http://localhost:8080");
    private final static MediaType json = MediaType.APPLICATION_JSON;

    private final static LocalDateTime now = LocalDateTime.now();
    private final static User user = new User(UUID.randomUUID(), now, "testEmail", true, "testFirstName", "testGender",
                                              1.75, now, "testLastName", "testPassword", "testUsername", 75.0);

    @PostMapping("/metric/activity")
    public Flux<Activity> activityInsert(@RequestParam(value = "amount") Integer amount) {
        Exercise exercise = new Exercise(UUID.randomUUID(), 5.0, "testCategory", "0", "testDescription");
        return Flux.range(0, amount)
                .flatMap(index -> Flux.just(new Activity(UUID.randomUUID(), exercise, user, 30, 300, now.plusDays(index))))
                .flatMap(activity -> writeWebClient.post()
                        .uri("/api/activities")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"exercise\":{" +
                                                      "\"compcode\":\"" + activity.getExercise().getCode() + "\"," +
                                                      "\"met\":" + activity.getExercise().getMet() + "," +
                                                      "\"category\":\"" + activity.getExercise().getCategory() + "\"," +
                                                      "\"description\":\"" + activity.getExercise().getDescription() +
                                                      "\"" +
                                                      "}," +
                                                      "\"user\":{" +
                                                      "\"username\":\"" + activity.getUser().getUsername() + "\"," +
                                                      "\"firstName\":\"" + activity.getUser().getFirstname() + "\"," +
                                                      "\"lastName\":\"" + activity.getUser().getLastname() + "\"," +
                                                      "\"weight\":" + activity.getUser().getWeight() + "," +
                                                      "\"height\":" + activity.getUser().getHeight() + "," +
                                                      "\"gender\":\"" + activity.getUser().getGender() + "\"," +
                                                      "\"birthday\":\"" +
                                                      activity.getUser().getBirthday().toLocalDate().toString() +
                                                      "\"," +
                                                      "\"email\":\"" + activity.getUser().getEmail() + "\"" +
                                                      "}," +
                                                      "\"duration\":" + activity.getTime() + "," +
                                                      "\"createdAt\":\"" + DateTimeFormatter
                                                              .ofPattern("yyyy-MM-dd HH:mm:ss")
                                                              .format(activity.getDate()) +
                                                      "\"" +
                                                      "}"))
                        .retrieve()
                        .bodyToFlux(Activity.class));
    }

    @PostMapping("/metric/diet")
    public Flux<Diet> dietInsert(@RequestParam(value = "amount") Integer amount) {
        Nutrition nutrition = new Nutrition(UUID.randomUUID(), "testName", 100.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        return Flux.range(0, amount)
                .flatMap(index -> Flux.just(new Diet(UUID.randomUUID(), user, nutrition, now.plusDays(index), 3, 300.0)))
                .flatMap(diet -> writeWebClient.post()
                        .uri("/api/diets")
                        .contentType(json)
                        .body(BodyInserters.fromValue("{" +
                                                      "\"nutrition\":{" +
                                                      "\"name\":\"" + diet.getNutrition().getName() + "\"," +
                                                      "\"calories\":" + diet.getNutrition().getCalories() + "," +
                                                      "\"fat\":" + diet.getNutrition().getFat() + "," +
                                                      "\"saturatedFat\":" + diet.getNutrition().getSaturatedFat() + "," +
                                                      "\"carbohydrates\":" + diet.getNutrition().getCarbohydrates() + "," +
                                                      "\"fiber\":" + diet.getNutrition().getFiber() + "," +
                                                      "\"sugar\":" + diet.getNutrition().getSugar() + "," +
                                                      "\"protein\":" + diet.getNutrition().getProtein() + "," +
                                                      "\"sodium\":" + diet.getNutrition().getSodium() + "" +
                                                      "}," +
                                                      "\"user\":{" +
                                                      "\"username\":\"" + diet.getUser().getUsername() + "\"," +
                                                      "\"firstName\":\"" + diet.getUser().getFirstname() + "\"," +
                                                      "\"lastName\":\"" + diet.getUser().getLastname() + "\"," +
                                                      "\"weight\":" + diet.getUser().getWeight() + "," +
                                                      "\"height\":" + diet.getUser().getHeight() + "," +
                                                      "\"gender\":\"" + diet.getUser().getGender() + "\"," +
                                                      "\"birthday\":\"" + diet.getUser().getBirthday().toLocalDate().toString() + "\"," +
                                                      "\"email\":\"" + diet.getUser().getEmail() + "\"" +
                                                      "}," +
                                                      "\"servingQuantity\":" + diet.getServingQty() + "," +
                                                      "\"createdAt\":\"" +
                                                      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(diet.getDate()) + "\"" +
                                                      "}"))
                        .retrieve()
                        .bodyToFlux(Diet.class));
    }
}
