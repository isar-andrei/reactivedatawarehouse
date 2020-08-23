package com.ai.dwsprintreactive.service.impl;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.model.Exercise;
import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.repository.ActivityRepository;
import com.ai.dwsprintreactive.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class ActivityServiceImpl implements ActivityService {

    @NotNull private final ActivityRepository repository;

    @Override
    public Mono<Activity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Activity> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Activity> save(Exercise exercise, User user, Integer duration, LocalDateTime createdAt) {
        Double caloriesBurned = exercise.getMet() * 3.5 * user.getWeight() / 200;

        return repository.save(Activity.builder()
                                       .exercise(exercise).user(user).duration(duration)
                                       .caloriesBurned(caloriesBurned).createdAt(createdAt)
                                       .build());
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(exercise -> repository.deleteById(exercise.getId()));
    }

    @Override
    public Mono<Void> deleteAll() {
        return findAll()
                .flatMap(diet -> deleteById(diet.getId()))
                .then();
    }
}
