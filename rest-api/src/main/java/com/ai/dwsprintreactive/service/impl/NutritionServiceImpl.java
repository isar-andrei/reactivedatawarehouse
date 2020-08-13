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
    public Mono<Nutrition> update(Integer id, Nutrition differentNutrition) {
        return get(id)
                .map(existingNutrition -> {
                    Integer nutritionId = existingNutrition.getId();
                    UUID nutritionUuid = existingNutrition.getUuid();
                    String name = differentNutrition.getName() != null ? differentNutrition.getName() : existingNutrition.getName();
                    Double calories = differentNutrition.getCalories() != null ? differentNutrition.getCalories() : existingNutrition.getCalories();
                    Double fat = differentNutrition.getFat() != null ? differentNutrition.getFat() : existingNutrition.getFat();
                    Double saturatedFat = differentNutrition.getSaturatedFat() != null ? differentNutrition.getSaturatedFat() : existingNutrition.getSaturatedFat();
                    Double carbohydrates = differentNutrition.getCarbohydrates() != null ? differentNutrition.getCarbohydrates() : existingNutrition.getCarbohydrates();
                    Double fiber = differentNutrition.getFiber() != null ? differentNutrition.getFiber() : existingNutrition.getFiber();
                    Double sugar = differentNutrition.getSugar() != null ? differentNutrition.getSugar() : existingNutrition.getSugar();
                    Double protein = differentNutrition.getProtein() != null ? differentNutrition.getProtein() : existingNutrition.getProtein();
                    Double sodium = differentNutrition.getSodium() != null ? differentNutrition.getSodium() : existingNutrition.getSodium();

                    return new Nutrition(nutritionId, nutritionUuid, name, calories, fat, saturatedFat, carbohydrates, fiber, sugar, protein, sodium);
                })
                .flatMap(repository::save);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return get(id)
                .flatMap(x -> repository.deleteById(x.getId()));
    }

    @Override
    public Mono<Nutrition> create(UUID uuid, String name, Double calories, Double fat, Double saturatedFat, Double carbohydrates,
                                  Double fiber, Double sugar, Double protein, Double sodium) {

        Nutrition nutrition = Nutrition.builder()
                .uuid(uuid).name(name).calories(calories).fat(fat).saturatedFat(saturatedFat)
                .carbohydrates(carbohydrates).fiber(fiber).sugar(sugar).protein(protein).sodium(sodium)
                .build();

        return repository.save(nutrition);
    }

}
