package com.ai.dwsprintreactive.service;


import com.ai.dwsprintreactive.model.Date;
import reactor.core.publisher.Mono;

public interface DateService {

    Mono<Date> get(Integer id);
}
