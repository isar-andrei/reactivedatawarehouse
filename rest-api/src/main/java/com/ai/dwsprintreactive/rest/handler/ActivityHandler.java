package com.ai.dwsprintreactive.rest.handler;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;

import static com.ai.dwsprintreactive.rest.RestURIs.ACTIVITY_URI;

@Component
@RequiredArgsConstructor
public class ActivityHandler extends AbstractHandler {

    @NotNull
    private final ActivityService service;

    public Mono<ServerResponse> all(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.all(), Activity.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return service.get(id(request))
                .flatMap(activity -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(activity), Activity.class))
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
        Mono<Activity> activityMono = request.bodyToMono(Activity.class)
                .flatMap(activity -> service.create(activity.getUuid(), activity.getExercise().getId(), activity.getUser().getId(),
                                                    activity.getDuration(), activity.getCreatedAt()));
        return Mono
                .from(activityMono)
                .flatMap(activity -> ServerResponse
                        .created(URI.create(ACTIVITY_URI + "/" + activity.getId()))
                        .contentType(json)
                        .build());
    }

}
