package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.repository.custom.ActivityCustomRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ActivityRepository extends ReactiveMongoRepository<Activity, String>, ActivityCustomRepository {
}
