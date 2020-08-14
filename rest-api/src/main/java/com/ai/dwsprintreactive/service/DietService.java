package com.ai.dwsprintreactive.service;


import com.ai.dwsprintreactive.model.Diet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DietService {

    Flux<Diet> all();

    Mono<Diet> get(Integer id);

    Flux<Diet> getByDate(Integer dateKey);

    Mono<Void> delete(Integer id);

    Mono<Diet> create(UUID uuid, Integer nutritionKey, Integer userKey, Double servingQuantity, LocalDateTime createdAt);
}
