package com.ai.dwsprintreactive.service.impl;

import com.ai.dwsprintreactive.model.Exercise;
import com.ai.dwsprintreactive.repository.ExerciseRepository;
import com.ai.dwsprintreactive.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("ExerciseService")
public class ExerciseServiceImpl implements ExerciseService {

    @NotNull
    private final ExerciseRepository repository;

    @Override
    public Flux<Exercise> all() {
        return repository.findAll();
    }

    @Override
    public Mono<Exercise> get(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Exercise> getByUuid(UUID uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public Mono<Exercise> update(Integer id, Exercise differentExercise) {
        return get(id)
                .map(existingExercise -> {
                    Integer exerciseId = existingExercise.getId();
                    UUID exerciseUuid = existingExercise.getUuid();
                    Integer compcode = differentExercise.getCompcode() != null ? differentExercise.getCompcode() : existingExercise.getCompcode();
                    Double met = differentExercise.getMet() != null ? differentExercise.getMet() : existingExercise.getMet();
                    String category = differentExercise.getCategory() != null ? differentExercise.getCategory() : existingExercise.getCategory();
                    String description = differentExercise.getDescription() != null ? differentExercise.getDescription() : existingExercise.getDescription();

                    return new Exercise(exerciseId, exerciseUuid, compcode, met, category, description);
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return get(id)
                .flatMap(exercise -> repository.deleteById(exercise.getId()));
    }

    @Override
    public Mono<Exercise> create(UUID uuid, Integer compcode, Double met, String category, String description) {
        Exercise exercise = Exercise.builder()
                .uuid(uuid).compcode(compcode).met(met)
                .category(category).description(description)
                .build();

        return repository.save(exercise);
    }
}
