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

    @NotNull private final ActivityService service;

    public Mono<ServerResponse> findActivityById(ServerRequest request) {
        return service.findById(id(request))
                .flatMap(activity -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(activity), Activity.class))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> findAllActivitiesByUsername(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.findAllByUsername(username(request)), Activity.class);
    }

    public Mono<ServerResponse> findAllActivitiesByExerciseCompcode(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.findAllByExerciseCompcode(request.pathVariable("exerciseCompcode")), Activity.class);
    }

    public Mono<ServerResponse> sumCalBurnedOnCurrentDay(ServerRequest request) {
        return service.sumCaloriesOnCurrentDay(username(request))
                .flatMap(calories -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(calories), Double.class));
    }

    public Mono<ServerResponse> avgCalBurnedOnCurrentWeek(ServerRequest request) {
        return service.avgCaloriesOnCurrentWeek(username(request))
                .flatMap(calories -> ServerResponse
                        .ok()
                        .contentType(json)
                        .body(Mono.just(calories), Double.class));
    }

    public Mono<ServerResponse> avgCalBurnedBetweenDates(ServerRequest request) {
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

    public Mono<ServerResponse> findAllActivities(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(json)
                .body(service.findAll(), Activity.class);
    }

    public Mono<ServerResponse> saveActivity(ServerRequest request) {
        Mono<Activity> activityMono = request.bodyToMono(Activity.class)
                .flatMap(activity -> service.save(activity.getExercise(), activity.getUser(), activity.getDuration(), activity.getCreatedAt()));
        return Mono
                .from(activityMono)
                .flatMap(activity -> ServerResponse
                        .created(URI.create(ACTIVITY_URI + "/" + activity.getId()))
                        .contentType(json)
                        .build());
    }

    public Mono<ServerResponse> deleteActivityById(ServerRequest request) {
        Mono<Void> result = service.deleteById(id(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> deleteActivityByUsername(ServerRequest request) {
        Mono<Void> result = service.deleteByUsername(username(request));
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }

    public Mono<ServerResponse> deleteAllActivities(ServerRequest request) {
        Mono<Void> result = service.deleteAll();
        return ServerResponse
                .ok()
                .contentType(json)
                .body(result, Void.class);
    }
}
