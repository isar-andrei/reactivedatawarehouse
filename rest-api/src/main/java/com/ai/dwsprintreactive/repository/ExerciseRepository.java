package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Exercise;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ExerciseRepository extends ReactiveCrudRepository<Exercise, Integer> {

    Mono<Exercise> findByUuid(UUID uuid);
}
