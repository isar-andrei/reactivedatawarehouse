package com.ai.dwsprintreactive.rest.handler;

import com.ai.dwsprintreactive.model.Exercise;
import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;

import static com.ai.dwsprintreactive.rest.RestURIs.EXERCISE_URI;
import static com.ai.dwsprintreactive.rest.RestURIs.USER_URI;

@Component
@RequiredArgsConstructor
public class ExerciseHandler extends AbstractHandler {

    @NotNull private final ExerciseService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), Exercise.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(exercise -> ServerResponse
                        .ok()
                        .body(Mono.just(exercise), Exercise.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getIdByUuid(ServerRequest request) {
        return service.getByUuid(uuid(request))
                .flatMap(exercise -> ServerResponse
                        .ok()
                        .body(Mono.just(exercise.getId()), Integer.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Flux<Exercise> exerciseFlux = request.bodyToFlux(Exercise.class)
                .flatMap(exercise -> service.update(id(request), Exercise.builder()
                        .compcode(exercise.getCompcode()).met(exercise.getMet())
                        .category(exercise.getCategory()).description(exercise.getDescription())
                        .build()));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(exerciseFlux, Exercise.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Mono<Exercise> exerciseMono = request.bodyToMono(Exercise.class)
                .flatMap(exercise -> service
                .create(exercise.getUuid(), exercise.getCompcode(), exercise.getMet(),
                        exercise.getCategory(), exercise.getDescription()));
        return Mono
                .from(exerciseMono)
                .flatMap(exercise -> ServerResponse
                        .created(URI.create(EXERCISE_URI + "/" + exercise.getId()))
                        .contentType(json)
                        .build());
    }
}
