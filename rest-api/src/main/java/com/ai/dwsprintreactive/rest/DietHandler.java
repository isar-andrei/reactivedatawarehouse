package com.ai.dwsprintreactive.rest;


import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.service.DietService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;

import static com.ai.dwsprintreactive.rest.RestURIs.DIET_URI;


@Component
@RequiredArgsConstructor
public class DietHandler {

    private final static MediaType json = MediaType.APPLICATION_JSON;
    private final static Mono<ServerResponse> notFound = ServerResponse.notFound().build();
    @NotNull private final DietService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), Diet.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(x -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(x), Diet.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getByDate(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.getByDate(id(request)), Diet.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Diet> result = request
                .bodyToMono(Diet.class)
                .flatMap(x -> service.create(x.getUuid(), x.getNutrition().getId(), x.getUser().getId(), x.getServingQuantity(),
                                          x.getCreatedAt()));
        return Mono
                .from(result)
                .flatMap(x -> ServerResponse
                        .created(URI.create(DIET_URI + "/" + x.getId()))
                        .contentType(json)
                        .build()
                );
    }

    private static Integer id(ServerRequest request) {
        return Integer.parseInt(request.pathVariable("id"));
    }
}
