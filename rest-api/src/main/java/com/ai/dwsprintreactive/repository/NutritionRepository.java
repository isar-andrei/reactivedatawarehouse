package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Nutrition;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface NutritionRepository extends ReactiveCrudRepository<Nutrition, Integer> {

    Mono<Nutrition> findNutritionByUuid(UUID uuid);

}
