package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.repository.custom.ActivityCustomRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ActivityRepository extends ReactiveCrudRepository<Activity, Integer>, ActivityCustomRepository {}
