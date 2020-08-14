package com.ai.dwsprintreactive.service.impl;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.repository.ActivityRepository;
import com.ai.dwsprintreactive.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("ActivityService")
public class ActivityServiceImpl implements ActivityService {

    @NotNull private final ActivityRepository repository;

    @Override
    public Flux<Activity> all() {
        return repository.all();
    }

    @Override
    public Mono<Activity> get(Integer id) {
        return repository.get(id);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return get(id)
                .flatMap(exercise -> repository.deleteById(exercise.getId()));
    }

    @Override
    public Mono<Activity> create(UUID uuid, Integer exerciseKey, Integer userKey, Integer duration, LocalDateTime createdAt) {
        return repository.save(uuid, exerciseKey, userKey, duration, createdAt);
    }
}
