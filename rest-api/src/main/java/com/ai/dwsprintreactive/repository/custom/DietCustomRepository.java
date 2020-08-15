package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Diet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;


public interface DietCustomRepository {

    Mono<Diet> get(Integer id);

    Flux<Diet> all();

    Mono<Diet> save(UUID uuid, Integer nutritionKey, Integer userKey, Double servingQuantity, LocalDateTime createdAt);
}