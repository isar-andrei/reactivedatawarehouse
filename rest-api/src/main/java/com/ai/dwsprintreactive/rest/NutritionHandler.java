package com.ai.dwsprintreactive.rest;

import com.ai.dwsprintreactive.model.Nutrition;
import com.ai.dwsprintreactive.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.UUID;

import static com.ai.dwsprintreactive.rest.RestURIs.NUTRITION_URI;


@Component
@RequiredArgsConstructor
public class NutritionHandler {

    private final static MediaType json = MediaType.APPLICATION_JSON;
    @NotNull private final NutritionService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), Nutrition.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(nutrition -> ServerResponse
                        .ok()
                        .body(Mono.just(nutrition), Nutrition.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getIdByUuid(ServerRequest request) {
        return service.getByUuid(UUID.fromString(request.pathVariable("uuid")))
                .flatMap(nutrition -> ServerResponse
                        .ok()
                        .body(Mono.just(nutrition.getId()), Integer.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    Mono<ServerResponse> updateById(ServerRequest request) {
        Flux<Nutrition> result = request.bodyToFlux(Nutrition.class)
                .flatMap(x -> service.update(id(request), Nutrition.builder()
                        .name(x.getName()).calories(x.getCalories()).fat(x.getFat())
                        .saturatedFat(x.getSaturatedFat()).carbohydrates(x.getCarbohydrates()).fiber(x.getFiber())
                        .sugar(x.getSugar()).protein(x.getProtein()).sodium(x.getSodium())
                        .build()));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Nutrition.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse.ok().contentType(json).body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<Nutrition> result = request
                .bodyToFlux(Nutrition.class)
                .flatMap(x -> service.create(x.getUuid(), x.getName(), x.getCalories(), x.getFat(),
                                             x.getSaturatedFat(), x.getCarbohydrates(), x.getFiber(),
                                             x.getSugar(), x.getProtein(), x.getSodium()));
        return Mono
                .from(result)
                .flatMap(x -> ServerResponse
                        .created(URI.create(NUTRITION_URI + "/" + x.getId()))
                        .contentType(json)
                        .build()
                );
    }

    private static Integer id(ServerRequest request) {
        return Integer.parseInt(request.pathVariable("id"));
    }
}
