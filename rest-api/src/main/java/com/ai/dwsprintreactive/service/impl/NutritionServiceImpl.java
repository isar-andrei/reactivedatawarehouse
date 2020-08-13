package com.ai.dwsprintreactive.service.impl;


import com.ai.dwsprintreactive.model.Nutrition;
import com.ai.dwsprintreactive.repository.NutritionRepository;
import com.ai.dwsprintreactive.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("NutritionService")
public class NutritionServiceImpl implements NutritionService {

    @NotNull
    private final NutritionRepository repository;

    @Override
    public Flux<Nutrition> all() {
        return repository.findAll();
    }

    @Override
    public Mono<Nutrition> get(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Nutrition> getByUuid(UUID uuid) {
        return repository.findNutritionByUuid(uuid);
    }

    @Override
    public Mono<Nutrition> update(Integer id, Nutrition entity) {
        return get(id)
                .map(x -> {
                    Integer nutritionId = x.getId();
                    UUID nutritionUuid = x.getUuid();
                    String name = entity.getName() != null ? entity.getName() : x.getName();
                    Double calories = entity.getCalories() != null ? entity.getCalories() : x.getCalories();
                    Double fat = entity.getFat() != null ? entity.getFat() : x.getFat();
                    Double saturatedFat = entity.getSaturatedFat() != null ? entity.getSaturatedFat() : x.getSaturatedFat();
                    Double carbohydrates = entity.getCarbohydrates() != null ? entity.getCarbohydrates() : x.getCarbohydrates();
                    Double fiber = entity.getFiber() != null ? entity.getFiber() : x.getFiber();
                    Double sugar = entity.getSugar() != null ? entity.getSugar() : x.getSugar();
                    Double protein = entity.getProtein() != null ? entity.getProtein() : x.getProtein();
                    Double sodium = entity.getSodium() != null ? entity.getSodium() : x.getSodium();

                    return new Nutrition(nutritionId, nutritionUuid, name, calories, fat, saturatedFat, carbohydrates, fiber, sugar, protein, sodium);
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return get(id)
                .flatMap(x -> repository.deleteById(x.getId())).log();
    }

    @Override
    public Mono<Nutrition> create(UUID uuid, String name, Double calories, Double fat, Double saturatedFat, Double carbohydrates,
                                  Double fiber, Double sugar, Double protein, Double sodium) {

        Nutrition entity = Nutrition.builder()
                .uuid(uuid).name(name).calories(calories).fat(fat).saturatedFat(saturatedFat)
                .carbohydrates(carbohydrates).fiber(fiber).sugar(sugar).protein(protein).sodium(sodium)
                .build();

        return repository.save(entity);
    }

}
