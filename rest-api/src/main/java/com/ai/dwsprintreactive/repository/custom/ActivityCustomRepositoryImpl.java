package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.model.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@RequiredArgsConstructor
public class ActivityCustomRepositoryImpl implements ActivityCustomRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Exercise> findAllByUsername(String username) {
        Query query = new Query(Criteria.where("user._id").is(username));
        return mongoTemplate.find(query, Exercise.class);
    }

    @Override
    public Flux<Exercise> findAllByExerciseCompcode(String compcode) {
        Query query = new Query(Criteria.where("compcode._id").is(compcode));
        return mongoTemplate.find(query, Exercise.class);
    }
}
