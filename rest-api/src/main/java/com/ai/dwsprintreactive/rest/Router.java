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
                .route(GET(DIET_URI).and(accept(json)), handler::findAllDiets)
                .andRoute(GET(DIET_URI + "/{id}").and(accept(json)), handler::findDietById)
                .andRoute(GET(DIET_URI + "/user/{username}").and(accept(json)), handler::findAllDietsByUsername)
                .andRoute(GET(DIET_URI + "/user/{username}/sumcalday").and(accept(json)), handler::sumCalConsumedOnCurrentDay)
                .andRoute(GET(DIET_URI + "/user/{username}/avgcal").and(accept(json)), handler::avgCalConsumedBetweenDates)
                .andRoute(GET(DIET_URI + "/user/{username}/avgcalweek").and(accept(json)), handler::avgCalConsumedOnCurrentWeek)
                .andRoute(GET(DIET_URI + "/nutrition/{nutritionName}").and(accept(json)), handler::findAllDietsByNutritionName)
                .andRoute(POST(DIET_URI).and(accept(json)), handler::saveDiet)
                .andRoute(DELETE(DIET_URI ).and(accept(json)), handler::deleteAllDiets)
                .andRoute(DELETE(DIET_URI + "/{id}").and(accept(json)), handler::deleteDietById)
                .andRoute(DELETE(DIET_URI + "/user/{username}").and(accept(json)), handler::deleteDietByUsername);
    }

    @Bean
    public RouterFunction<ServerResponse> activityEndpoint(ActivityHandler handler) {
        return RouterFunctions
                .route(GET(ACTIVITY_URI).and(accept(json)), handler::findAllActivities)
                .andRoute(GET(ACTIVITY_URI + "/{id}").and(accept(json)), handler::findActivityById)
                .andRoute(GET(ACTIVITY_URI + "/user/{username}").and(accept(json)), handler::findAllActivitiesByUsername)
                .andRoute(GET(ACTIVITY_URI + "/user/{username}/sumcalday").and(accept(json)), handler::sumCalBurnedOnCurrentDay)
                .andRoute(GET(ACTIVITY_URI + "/user/{username}/avgcal").and(accept(json)), handler::avgCalBurnedBetweenDates)
                .andRoute(GET(ACTIVITY_URI + "/user/{username}/avgcalweek").and(accept(json)), handler::avgCalBurnedOnCurrentWeek)
                .andRoute(GET(ACTIVITY_URI + "/exercise/{exerciseCompcode}").and(accept(json)), handler::findAllActivitiesByExerciseCompcode)
                .andRoute(POST(ACTIVITY_URI).and(accept(json)), handler::saveActivity)
                .andRoute(DELETE(ACTIVITY_URI ).and(accept(json)), handler::deleteAllActivities)
                .andRoute(DELETE(ACTIVITY_URI + "/{id}").and(accept(json)), handler::deleteActivityById)
                .andRoute(DELETE(ACTIVITY_URI + "/user/{username}").and(accept(json)), handler::deleteActivityByUsername);
    }
}
