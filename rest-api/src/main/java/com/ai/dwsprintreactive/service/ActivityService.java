package com.ai.dwsprintreactive.service;

import com.ai.dwsprintreactive.model.Activity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ActivityService {

    Flux<Activity> all();

    Mono<Activity> get(Integer id);

    Mono<Void> delete(Integer id);

    Mono<Activity> create(UUID uuid, Integer exerciseKey, Integer userKey, Integer duration, LocalDateTime createdAt);
}
