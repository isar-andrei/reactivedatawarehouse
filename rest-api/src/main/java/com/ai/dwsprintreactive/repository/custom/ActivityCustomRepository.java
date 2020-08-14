package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Activity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ActivityCustomRepository {

    Mono<Activity> get(Integer id);

    Flux<Activity> all();

    Mono<Activity> save(UUID uuid, Integer exerciseKey, Integer userKey, Integer duration, LocalDateTime createdAt);
}
