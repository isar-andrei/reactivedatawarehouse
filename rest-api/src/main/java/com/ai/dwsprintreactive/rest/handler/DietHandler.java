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

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.findById(id(request))
                .flatMap(diet -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(diet), Diet.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAllByUsername(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.findAllByUsername(username(request)), Diet.class);
    }

    public Mono<ServerResponse> findAllByNutritionName(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.findAllByNutritionName(request.pathVariable("nutritionName")), Diet.class);
    }

    public Mono<ServerResponse> sumCaloriesOnCurrentDay(ServerRequest request) {
        return service.sumCaloriesOnCurrentDay(username(request))
                .flatMap(calories -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(calories), Double.class));
    }

    public Mono<ServerResponse> avgCaloriesOnCurrentWeek(ServerRequest request) {
        return service.avgCaloriesOnCurrentWeek(username(request))
                .flatMap(calories -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(calories), Double.class));
    }

    public Mono<ServerResponse> avgCaloriesBetweenDates(ServerRequest request) {
        String starting;
        String ending;
       if (request.queryParam("starting").isPresent() && request.queryParam("ending").isPresent()) {
           starting = request.queryParam("starting").get();
           ending = request.queryParam("ending").get();
       } else {
           return ServerResponse
                   .ok()
                   .contentType(json)
                   .body(Mono.empty(), Double.class);
       }

        return service.avgCaloriesBetweenDates(request.pathVariable("username"), starting, ending)
                .flatMap(calories -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(calories), Double.class));
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.findAll(), Diet.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Diet> dietMono = request
                .bodyToMono(Diet.class)
                .flatMap(diet -> service.save(diet.getNutrition(), diet.getUser(), diet.getServingQuantity(), diet.getCreatedAt()));
        return Mono
                .from(dietMono)
                .flatMap(diet -> ServerResponse
                        .created(URI.create(DIET_URI + "/" + diet.getId()))
                        .contentType(json)
                        .build()
                );
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        Mono<Void> result = service.deleteById(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> deleteByUsername(ServerRequest request) {
        Mono<Void> result = service.deleteByUsername(username(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> deleteAll(ServerRequest request) {
        Mono<Void> result = service.deleteAll();
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }
}
