package com.ai.dwsprintreactive.service.impl;


import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.model.Nutrition;
import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.repository.DietRepository;
import com.ai.dwsprintreactive.service.DietService;
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
public class DietServiceImpl implements DietService {

    @NotNull private final DietRepository repository;

    @Override
    public Mono<Diet> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Diet> findAllByUsername(String username) {
        return repository.findAllByUsername(username);
    }

    @Override
    public Flux<Diet> findAllByNutritionName(String nutritionName) {
        return repository.findAllByNutritionName(nutritionName);
    }

    @Override
    public Mono<Double> sumCaloriesOnCurrentDay(String username) {
        int currentDay = LocalDate.now().getDayOfYear();

        Flux<Diet> result = findAllByUsername(username)
                .filter(diet -> diet.getCreatedAt().toLocalDate().getDayOfYear() == currentDay);

        return result.collect(Collectors.summingDouble(Diet::getCaloriesConsumed));
    }

    @Override
    public Mono<Double> avgCaloriesOnCurrentWeek(String username) {
        LocalDate now = LocalDate.now();
        LocalDate starting = now.with(DayOfWeek.MONDAY);
        LocalDate ending = now.with(DayOfWeek.SUNDAY);

        Flux<Diet> result = findAllByUsername(username)
                .filter(diet -> !diet.getCreatedAt().toLocalDate().isBefore(starting) && !diet.getCreatedAt().toLocalDate().isAfter(ending));

        return result.collect(Collectors.averagingDouble(Diet::getCaloriesConsumed));
    }

    @Override
    public Mono<Double> avgCaloriesBetweenDates(String username, String starting, String ending) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startingDate = LocalDateTime.parse(starting, formatter);
        LocalDateTime endingDate = LocalDateTime.parse(ending, formatter);

        Flux<Diet> result = findAllByUsername(username)
                .filter(diet -> diet.getCreatedAt().isAfter(startingDate) && diet.getCreatedAt().isBefore(endingDate));

        return result.collect(Collectors.averagingDouble(Diet::getCaloriesConsumed));
    }

    @Override
    public Flux<Diet> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Diet> save(Nutrition nutrition, User user, Double servingQuantity, LocalDateTime createdAt) {
        Double caloriesConsumed = servingQuantity * nutrition.getCalories();
        return repository.save(Diet.builder()
                                       .nutrition(nutrition).user(user).servingQuantity(servingQuantity)
                                       .caloriesConsumed(caloriesConsumed).createdAt(createdAt)
                                       .build());
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return findById(id)
                .flatMap(diet -> repository.deleteById(diet.getId()));
    }

    @Override
    public Mono<Void> deleteByUsername(String username) {
        return findAllByUsername(username)
                .flatMap(diet -> deleteById(diet.getId()))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        return findAll()
                .flatMap(diet -> deleteById(diet.getId()))
                .then();
    }

//    public <T> Flux<T> intersect(Flux<T> f1, Flux<T> f2) {
//        return f1.join(f2, f -> Flux.never(), f -> Flux.never(), Tuples::of)
//                .filter(t -> t.getT1().equals(t.getT2()))
//                .map(Tuple2::getT1)
//                .groupBy(f -> f)
//                .map(GroupedFlux::key);
//    }
}
