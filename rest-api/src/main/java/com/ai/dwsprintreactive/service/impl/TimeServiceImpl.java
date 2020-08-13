package com.ai.dwsprintreactive.service.impl;


import com.ai.dwsprintreactive.model.Time;
import com.ai.dwsprintreactive.repository.TimeRepository;
import com.ai.dwsprintreactive.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component("TimeService")
public class TimeServiceImpl implements TimeService {

    @NotNull private final TimeRepository repository;

    @Override
    public Mono<Time> get(Integer id) {
        return repository.findById(id);
    }
}
