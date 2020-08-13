package com.ai.dwsprintreactive.service;


import com.ai.dwsprintreactive.model.Nutrition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NutritionService {

    Flux<Nutrition> all();

    Mono<Nutrition> get(Integer id);

    Mono<Nutrition> getByUuid(UUID uuid);

    Mono<Nutrition> update(Integer id, Nutrition differentNutrition);

    Mono<Void> delete(Integer id);

    Mono<Nutrition> create(UUID uuid, String name, Double calories, Double fat, Double saturatedFat, Double carbohydrates,
                           Double fiber, Double sugar, Double protein, Double sodium);
}
