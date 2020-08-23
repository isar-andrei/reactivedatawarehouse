package com.ai.dwsprintreactive.rest.handler;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public abstract class AbstractHandler {

    protected final static MediaType json = MediaType.APPLICATION_JSON;
    protected final static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    protected static String id(ServerRequest request) {
        return request.pathVariable("id");
    }

    protected static String username(ServerRequest request) {
        return request.pathVariable("username");
    }
}
