package com.ai.dwsprintreactive.repository;

import com.ai.dwsprintreactive.model.Date;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface DateRepository extends ReactiveCrudRepository<Date, Integer> {}
