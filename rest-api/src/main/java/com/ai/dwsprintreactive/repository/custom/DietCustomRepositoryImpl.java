package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Diet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class DietCustomRepositoryImpl implements DietCustomRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Diet> findAllByUsername(String username) {
        Query query = new Query(Criteria.where("user._id").is(username));
        return mongoTemplate.find(query, Diet.class);
    }

    @Override
    public Flux<Diet> findAllByNutritionName(String nutritionName) {
        Query query = new Query(Criteria.where("nutrition._id").is(nutritionName));
        return mongoTemplate.find(query, Diet.class);
    }
}
