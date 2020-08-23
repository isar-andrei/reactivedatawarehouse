package com.ai.dwsprintreactive.service;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.model.Exercise;
import com.ai.dwsprintreactive.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ActivityService {

    Mono<Activity> findById(String id);

    Flux<Activity> findAllByUsername(String username);

    Flux<Activity> findAllByExerciseCompcode(String exerciseName);

    Mono<Double> sumCaloriesOnCurrentDay(String username);

    Mono<Double> avgCaloriesOnCurrentWeek(String username);

    Mono<Double> avgCaloriesBetweenDates(String username, String starting, String ending);

    Flux<Activity> findAll();

    Mono<Activity> save(Exercise exercise, User user, Integer duration, LocalDateTime createdAt);

    Mono<Void> deleteById(String id);

    Mono<Void> deleteByUsername(String username);

    Mono<Void> deleteAll();
}
