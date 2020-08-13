package com.ai.dwsprintreactive.repository;


import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.repository.custom.DietCustomRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietRepository extends ReactiveCrudRepository<Diet, Integer>, DietCustomRepository {}
