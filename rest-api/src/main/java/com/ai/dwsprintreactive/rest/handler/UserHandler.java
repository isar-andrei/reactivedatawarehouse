package com.ai.dwsprintreactive.rest.handler;

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
public class UserHandler extends AbstractHandler {

    @NotNull private final UserService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), User.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(user -> ServerResponse
                        .ok()
                        .body(Mono.just(user), User.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getIdByUuid(ServerRequest request) {
        return service.getByUuid(uuid(request))
                .flatMap(user -> ServerResponse
                        .ok()
                        .body(Mono.just(user.getId()), Integer.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        Flux<User> userFlux = request.bodyToFlux(User.class)
                .flatMap(user -> service.update(id(request), User.builder()
                        .firstName(user.getFirstName()).lastName(user.getLastName()).weight(user.getWeight())
                        .height(user.getHeight()).gender(user.getGender()).birthday(user.getBirthday())
                        .username(user.getUsername()).email(user.getEmail())
                        .build()));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(userFlux, User.class);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Mono<Void> result = service.delete(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        Flux<User> userFlux = request.bodyToFlux(User.class)
                .flatMap(user -> service
                        .create(user.getUuid(), user.getFirstName(), user.getLastName(), user.getWeight(), user.getHeight(),
                                user.getGender(), user.getBirthday(), user.getUsername(), user.getEmail()));
        return Mono
                .from(userFlux)
                .flatMap(user -> ServerResponse
                        .created(URI.create(USER_URI + "/" + user.getId()))
                        .contentType(json)
                        .build());
    }
}
