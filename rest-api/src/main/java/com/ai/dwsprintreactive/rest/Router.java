package com.ai.dwsprintreactive.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.ai.dwsprintreactive.rest.RestURIs.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {

    private final MediaType json = MediaType.APPLICATION_JSON;

    @Bean
    public RouterFunction<ServerResponse> dietEndpoint(DietHandler handler) {
        return RouterFunctions
                .route(GET(DIET_URI).and(accept(json)), handler::all)
                .andRoute(GET(DIET_URI + "/{id}").and(accept(json)), handler::getById)
                .andRoute(GET(DIET_URI + "/{id}").and(accept(json)), handler::getByDate)
                .andRoute(POST(DIET_URI).and(accept(json)), handler::create)
                .andRoute(DELETE(DIET_URI + "/{id}").and(accept(json)), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> nutritionEndpoint(NutritionHandler handler) {
        return RouterFunctions
                .route(GET(NUTRITION_URI).and(accept(json)), handler::all)
                .andRoute(GET(NUTRITION_URI + "/{id}").and(accept(json)), handler::getById)
                .andRoute(GET(NUTRITION_URI + "/convertUUIDtoID/{uuid}").and(accept(json)), handler::getIdByUuid)
                .andRoute(PUT(NUTRITION_URI + "/{id}").and(accept(json)), handler::updateById)
                .andRoute(POST(NUTRITION_URI).and(accept(json)), handler::create)
                .andRoute(DELETE(NUTRITION_URI + "/{id}").and(accept(json)), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> userEndpoint(UserHandler handler) {
        return RouterFunctions
                .route(GET(USER_URI).and(accept(json)), handler::all)
                .andRoute(GET(USER_URI + "/{id}").and(accept(json)), handler::getById)
                .andRoute(GET(USER_URI + "/convertUUIDtoID/{uuid}").and(accept(json)), handler::getIdByUuid)
                .andRoute(PUT(USER_URI + "/{id}").and(accept(json)), handler::updateById)
                .andRoute(POST(USER_URI).and(accept(json)), handler::create)
                .andRoute(DELETE(USER_URI + "/{id}").and(accept(json)), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> timeEndpoint(TimeHandler handler) {
        return  RouterFunctions
                .route(GET(TIME_URI + "/{id}").and(accept(json)), handler::getById);
    }

    @Bean
    public RouterFunction<ServerResponse> dateEndpoint(DateHandler handler) {
        return  RouterFunctions
                .route(GET(DATE_URI + "/{id}").and(accept(json)), handler::getById);
    }
}