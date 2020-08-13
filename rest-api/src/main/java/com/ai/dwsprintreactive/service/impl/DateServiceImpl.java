package com.ai.dwsprintreactive.service.impl;


import com.ai.dwsprintreactive.model.Date;
import com.ai.dwsprintreactive.repository.DateRepository;
import com.ai.dwsprintreactive.service.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("DateService")
public class DateServiceImpl implements DateService {

    @NotNull
    private final DateRepository repository;

    @Override
    public Mono<Date> get(Integer id) {
        return repository.findById(id);
    }
}
