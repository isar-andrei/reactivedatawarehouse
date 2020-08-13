package com.ai.dwsprintreactive.service.impl;


import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.repository.DietRepository;
import com.ai.dwsprintreactive.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("DietService")
public class DietServiceImpl implements DietService {

    @NotNull private final DietRepository repository;

    @Override
    public Flux<Diet> all() {
        return repository.all();
    }

    @Override
    public Mono<Diet> get(Integer id) {
        return repository.get(id);
    }

    //TODO: Implement
    @Override
    public Flux<Diet> getByDate(Integer dateKey) {
        return repository.findAllDietByDay(dateKey).log();
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return get(id)
                .flatMap(x -> repository.deleteById(x.getId()));
    }

    @Override
    public Mono<Diet> create(UUID uuid, Integer nutritionKey, Integer userKey, Double servingQuantity, LocalDateTime dietCreatedAt) {
        return repository.save(uuid, nutritionKey, userKey, servingQuantity, dietCreatedAt);
    }
}
