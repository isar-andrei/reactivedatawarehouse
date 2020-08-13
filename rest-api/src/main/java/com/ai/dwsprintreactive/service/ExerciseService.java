package com.ai.dwsprintreactive.service;

import com.ai.dwsprintreactive.model.Exercise;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ExerciseService {

    Flux<Exercise> all();

    Mono<Exercise> get(Integer id);

    Mono<Exercise> getByUuid(UUID uuid);

    Mono<Exercise> update(Integer id, Exercise differentExercise);

    Mono<Void> delete(Integer id);

    Mono<Exercise> create(UUID uuid, Integer compcode, Double met, String category, String description);
}
