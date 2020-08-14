package com.ai.dwsprintreactive.rest.handler;

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
public class NutritionHandler extends AbstractHandler {

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
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getIdByUuid(ServerRequest request) {
        return service.getByUuid(uuid(request))
                .flatMap(nutrition -> ServerResponse
                        .ok()
                        .body(Mono.just(nutrition.getId()), Integer.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Flux<Nutrition> nutritionFlux = request.bodyToFlux(Nutrition.class)
                .flatMap(nutrition -> service.update(id(request), Nutrition.builder()
                        .name(nutrition.getName()).calories(nutrition.getCalories()).fat(nutrition.getFat())
                        .saturatedFat(nutrition.getSaturatedFat()).carbohydrates(nutrition.getCarbohydrates()).fiber(nutrition.getFiber())
                        .sugar(nutrition.getSugar()).protein(nutrition.getProtein()).sodium(nutrition.getSodium())
                        .build()));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(nutritionFlux, Nutrition.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Nutrition> nutritionMono = request.bodyToMono(Nutrition.class)
                .flatMap(nutrition -> service.create(nutrition.getUuid(), nutrition.getName(), nutrition.getCalories(), nutrition.getFat(),
                                             nutrition.getSaturatedFat(), nutrition.getCarbohydrates(), nutrition.getFiber(),
                                             nutrition.getSugar(), nutrition.getProtein(), nutrition.getSodium()));
        return Mono
                .from(nutritionMono)
                .flatMap(nutrition -> ServerResponse
                        .created(URI.create(NUTRITION_URI + "/" + nutrition.getId()))
                        .contentType(json)
                        .build()
                );
    }
}
