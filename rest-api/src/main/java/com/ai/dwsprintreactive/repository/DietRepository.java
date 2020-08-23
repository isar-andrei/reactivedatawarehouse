package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.repository.custom.DietCustomRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DietRepository extends ReactiveMongoRepository<Diet, String>, DietCustomRepository {

}
