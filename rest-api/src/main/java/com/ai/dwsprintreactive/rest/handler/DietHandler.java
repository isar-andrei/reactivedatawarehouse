package com.ai.dwsprintreactive.rest.handler;


import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;

import static com.ai.dwsprintreactive.rest.RestURIs.DIET_URI;


@Component
@RequiredArgsConstructor
public class DietHandler extends AbstractHandler {

    @NotNull private final DietService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), Diet.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(diet -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(diet), Diet.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Diet> dietMono = request
                .bodyToMono(Diet.class)
                .flatMap(diet -> service.create(diet.getUuid(), diet.getNutrition().getId(), diet.getUser().getId(),
                                                diet.getServingQuantity(), diet.getCreatedAt()));
        return Mono
                .from(dietMono)
                .flatMap(diet -> ServerResponse
                        .created(URI.create(DIET_URI + "/" + diet.getId()))
                        .contentType(json)
                        .build()
                );
    }
}
