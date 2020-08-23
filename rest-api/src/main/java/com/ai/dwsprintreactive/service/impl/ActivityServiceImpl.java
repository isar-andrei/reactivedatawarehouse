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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class ActivityServiceImpl implements ActivityService {

    @NotNull private final ActivityRepository repository;

    @Override
    public Mono<Activity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Activity> findAllByUsername(String username) {
        return repository.findAllByUsername(username);
    }

    @Override
    public Flux<Activity> findAllByExerciseCompcode(String compcode) {
        return repository.findAllByExerciseCompcode(compcode);
    }

    @Override
    public Mono<Double> sumCaloriesOnCurrentDay(String username) {
        int currentDay = LocalDate.now().getDayOfYear();

        Flux<Activity> result = findAllByUsername(username)
                .filter(activity -> activity.getCreatedAt().toLocalDate().getDayOfYear() == currentDay);

        return result.collect(Collectors.summingDouble(Activity::getCaloriesBurned));
    }

    @Override
    public Mono<Double> avgCaloriesOnCurrentWeek(String username) {
        LocalDate now = LocalDate.now();
        LocalDate starting = now.with(DayOfWeek.MONDAY);
        LocalDate ending = now.with(DayOfWeek.SUNDAY);

        Flux<Activity> result = findAllByUsername(username)
                .filter(activity -> !activity.getCreatedAt().toLocalDate().isBefore(starting) && !activity.getCreatedAt().toLocalDate().isAfter(ending));

        return result.collect(Collectors.averagingDouble(Activity::getCaloriesBurned));
    }

    @Override
    public Mono<Double> avgCaloriesBetweenDates(String username, String starting, String ending) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startingDate = LocalDateTime.parse(starting, formatter);
        LocalDateTime endingDate = LocalDateTime.parse(ending, formatter);

        Flux<Activity> result = findAllByUsername(username)
                .filter(activity -> activity.getCreatedAt().isAfter(startingDate) && activity.getCreatedAt().isBefore(endingDate));

        return result.collect(Collectors.averagingDouble(Activity::getCaloriesBurned));
    }

    @Override
    public Flux<Activity> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Activity> save(Exercise exercise, User user, Integer duration, LocalDateTime createdAt) {
        Double caloriesBurned = exercise.getMet() * 3.5 * user.getWeight() / 200 * duration;

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
    public Mono<Void> deleteByUsername(String username) {
        return findAllByUsername(username)
                .flatMap(activity -> deleteById(activity.getId()))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return findAll()
                .flatMap(diet -> deleteById(diet.getId()))
                .then();
    }
}
