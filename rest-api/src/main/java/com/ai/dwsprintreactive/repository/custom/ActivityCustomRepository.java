package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Exercise;
import reactor.core.publisher.Flux;

public interface ActivityCustomRepository {

    Flux<Exercise> findAllByUsername(String username);

    Flux<Exercise> findAllByExerciseCompcode(String compcode);

}
