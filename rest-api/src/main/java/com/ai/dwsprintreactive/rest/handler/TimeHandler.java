package com.ai.dwsprintreactive.rest.handler;

import com.ai.dwsprintreactive.model.Time;
import com.ai.dwsprintreactive.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

@Component
@RequiredArgsConstructor
public class TimeHandler extends AbstractHandler {

    @NotNull private final TimeService service;

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(time -> ServerResponse
                        .ok()
                        .body(Mono.just(time), Time.class))
                .switchIfEmpty(notFound);
    }
}
