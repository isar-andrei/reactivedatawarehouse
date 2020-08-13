package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Time;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeRepository extends ReactiveCrudRepository<Time, Integer> {}
