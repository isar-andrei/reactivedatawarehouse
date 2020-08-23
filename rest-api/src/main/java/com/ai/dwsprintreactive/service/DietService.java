package com.ai.dwsprintreactive.service;


import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.model.Nutrition;
import com.ai.dwsprintreactive.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface DietService {

    Mono<Diet> findById(String id);

    Flux<Diet> findAllByUsername(String username);

    Flux<Diet> findAllByNutritionName(String nutritionName);

    Mono<Double> sumCaloriesOnCurrentDay(String username);

    Mono<Double> avgCaloriesOnCurrentWeek(String username);

    Mono<Double> avgCaloriesBetweenDates(String username, String starting, String ending);

    Flux<Diet> findAll();

    Mono<Diet> save(Nutrition nutrition, User user, Double servingQuantity, LocalDateTime createdAt);

    Mono<Void> deleteById(String id);

    Mono<Void> deleteByUsername(String username);

    Mono<Void> deleteAll();
}
