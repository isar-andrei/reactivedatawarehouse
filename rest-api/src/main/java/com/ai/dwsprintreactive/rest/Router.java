package com.ai.dwsprintreactive.rest;

import com.ai.dwsprintreactive.rest.handler.ActivityHandler;
import com.ai.dwsprintreactive.rest.handler.DietHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.ai.dwsprintreactive.rest.RestURIs.ACTIVITY_URI;
import static com.ai.dwsprintreactive.rest.RestURIs.DIET_URI;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class Router {

    private final MediaType json = MediaType.APPLICATION_JSON;

    @Bean
    public RouterFunction<ServerResponse> dietEndpoint(DietHandler handler) {
        return RouterFunctions
                .route(GET(DIET_URI + "/{id}").and(accept(json)), handler::getById)
                .andRoute(GET(DIET_URI + "/user/{username}").and(accept(json)), handler::findAllByUsername)
                .andRoute(GET(DIET_URI + "/user/{username}/sumcalday").and(accept(json)), handler::sumCaloriesOnCurrentDay)
                .andRoute(GET(DIET_URI + "/user/{username}/avgcalweek").and(accept(json)), handler::avgCaloriesOnCurrentWeek)
                .andRoute(GET(DIET_URI + "/user/{username}/avgcal").and(accept(json)), handler::avgCaloriesBetweenDates)
                .andRoute(GET(DIET_URI + "/nutrition/{nutritionName}").and(accept(json)), handler::findAllByNutritionName)
                .andRoute(GET(DIET_URI).and(accept(json)), handler::findAll)
                .andRoute(POST(DIET_URI).and(accept(json)), handler::create)
                .andRoute(DELETE(DIET_URI + "/{id}").and(accept(json)), handler::deleteById)
                .andRoute(DELETE(DIET_URI + "/user/{username}").and(accept(json)), handler::deleteByUsername)
                .andRoute(DELETE(DIET_URI ).and(accept(json)), handler::deleteAll);
    }

    @Bean
    public RouterFunction<ServerResponse> activityEndpoint(ActivityHandler handler) {
        return RouterFunctions
                .route(GET(ACTIVITY_URI + "/{id}").and(accept(json)), handler::getById)
                .andRoute(GET(ACTIVITY_URI).and(accept(json)), handler::all)
                .andRoute(POST(ACTIVITY_URI).and(accept(json)), handler::create)
                .andRoute(DELETE(ACTIVITY_URI + "/{id}").and(accept(json)), handler::delete)
                .andRoute(DELETE(ACTIVITY_URI ).and(accept(json)), handler::deleteAll);
    }
}
