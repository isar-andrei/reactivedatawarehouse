package com.ai.dwsprintreactive.rest;

import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.service.UserService;
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

import static com.ai.dwsprintreactive.rest.RestURIs.USER_URI;


@Component
@RequiredArgsConstructor
public class UserHandler {

    private final static MediaType json = MediaType.APPLICATION_JSON;
    @NotNull private final UserService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), User.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(x -> ServerResponse
                        .ok()
                        .body(Mono.just(x), User.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getIdByUuid(ServerRequest request) {
        return service.getByUuid(UUID.fromString(request.pathVariable("uuid")))
                .flatMap(user -> ServerResponse
                        .ok()
                        .body(Mono.just(user.getId()), Integer.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    Mono<ServerResponse> updateById(ServerRequest request) {
        Flux<User> result = request.bodyToFlux(User.class)
                .flatMap(x -> service.update(id(request), User.builder()
                        .firstName(x.getFirstName()).lastName(x.getLastName()).weight(x.getWeight())
                        .height(x.getHeight()).gender(x.getGender()).birthday(x.getBirthday())
                        .username(x.getUsername()).email(x.getEmail())
                        .build()));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, User.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse.ok().contentType(json).body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<User> result = request
                .bodyToFlux(User.class)
                .flatMap(x -> service
                        .create(x.getUuid(), x.getFirstName(), x.getLastName(), x.getWeight(), x.getHeight(), x.getGender(),
                                x.getBirthday(), x.getUsername(), x.getEmail()));
        return Mono
                .from(result)
                .flatMap(x -> ServerResponse
                        .created(URI.create(USER_URI + "/" + x.getId()))
                        .contentType(json)
                        .build());
    }

    private static Integer id(ServerRequest request) {
        return Integer.parseInt(request.pathVariable("id"));
    }
}
