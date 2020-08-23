package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Activity;
import reactor.core.publisher.Flux;

public interface ActivityCustomRepository {

    Flux<Activity> findAllByUsername(String username);

    Flux<Activity> findAllByExerciseCompcode(String compcode);

}
