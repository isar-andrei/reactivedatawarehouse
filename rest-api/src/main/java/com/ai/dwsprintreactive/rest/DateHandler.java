package com.ai.dwsprintreactive.rest;


import com.ai.dwsprintreactive.model.Date;
import com.ai.dwsprintreactive.service.DateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class DateHandler {

    @NotNull private final DateService service;

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(date -> ServerResponse
                        .ok()
                        .body(Mono.just(date), Date.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private static Integer id(ServerRequest request) {
        return Integer.parseInt(request.pathVariable("id"));
    }
}
