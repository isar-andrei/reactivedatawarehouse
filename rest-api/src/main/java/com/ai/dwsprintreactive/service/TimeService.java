package com.ai.dwsprintreactive.service;


import com.ai.dwsprintreactive.model.Time;
import reactor.core.publisher.Mono;

public interface TimeService {

    Mono<Time> get(Integer id);
}
