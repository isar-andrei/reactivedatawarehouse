package com.ai.dwsprintreactive.rest.handler;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public abstract class AbstractHandler {

    protected final static MediaType json = MediaType.APPLICATION_JSON;
    protected final static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    protected static Integer id(ServerRequest request) {
        return Integer.parseInt(request.pathVariable("id"));
    }

    protected static UUID uuid(ServerRequest request) { return UUID.fromString(request.pathVariable("uuid")); }
}
