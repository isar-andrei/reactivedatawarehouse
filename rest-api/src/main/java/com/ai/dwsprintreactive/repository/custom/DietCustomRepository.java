package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Diet;
import reactor.core.publisher.Flux;


public interface DietCustomRepository {

    Flux<Diet> findAllByUsername(String username);

    Flux<Diet> findAllByNutritionName(String nutritionName);
}
