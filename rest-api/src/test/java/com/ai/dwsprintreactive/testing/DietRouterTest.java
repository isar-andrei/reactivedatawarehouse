package com.ai.dwsprintreactive.testing;

import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.model.Nutrition;
import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.repository.ActivityRepository;
import com.ai.dwsprintreactive.rest.Router;
import com.ai.dwsprintreactive.rest.handler.ActivityHandler;
import com.ai.dwsprintreactive.rest.handler.DietHandler;
import com.ai.dwsprintreactive.service.impl.ActivityServiceImpl;
import com.ai.dwsprintreactive.service.impl.DietServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.ai.dwsprintreactive.rest.RestURIs.DIET_URI;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Router.class, ActivityHandler.class, DietHandler.class})
@WebFluxTest
public class DietRouterTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private ActivityServiceImpl activityService;

    @MockBean
    private DietServiceImpl dietService;

    @MockBean
    private ActivityRepository activityRepository;

    private WebTestClient webTestClient;

    private final static MediaType json = MediaType.APPLICATION_JSON;

    @BeforeAll
    public static void setUp(){
        BlockHound.install();
    }

    @BeforeEach
    public void initialize() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void blockhoundWorks() {
        try {
            Mono.delay(Duration.ofSeconds(1))
                    .doOnNext(it -> {
                        try {
                            Thread.sleep(10);
                        }
                        catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .block();
            Assertions.fail("Blockhound doesn't work");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @Test
    public void findAllDiets_test() {
        User actualUser = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Nutrition actualNutrition = new Nutrition("pizza", 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet1 = new Diet("1", actualNutrition, actualUser, 4.0, 567.0, LocalDateTime.now());
        Diet actualDiet2 = new Diet("2", actualNutrition, actualUser, 3.0, 123.4, LocalDateTime.now());
        Diet actualDiet3 = new Diet("3", actualNutrition, actualUser, 5.0, 854.4, LocalDateTime.now());

        BDDMockito
                .when(dietService.findAll()).thenReturn(Flux.just(actualDiet1, actualDiet2, actualDiet3));

        webTestClient.get()
                .uri(DIET_URI)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Diet.class)
                .value(dietResponse -> {
                    Assertions.assertEquals(3, dietResponse.size());
                    Assertions.assertEquals(actualUser, dietResponse.get(0).getUser());
                    Assertions.assertEquals(actualUser, dietResponse.get(1).getUser());
                    Assertions.assertEquals(actualUser, dietResponse.get(2).getUser());
                    Assertions.assertEquals(actualNutrition, dietResponse.get(0).getNutrition());
                    Assertions.assertEquals(actualNutrition, dietResponse.get(1).getNutrition());
                    Assertions.assertEquals(actualNutrition, dietResponse.get(2).getNutrition());
                });
    }

    @Test
    public void findDietById_test() {
        String actualId = "1";
        User actualUser = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Nutrition actualNutrition = new Nutrition("pizza", 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet = new Diet(actualId, actualNutrition, actualUser, 4.0, 567.0, LocalDateTime.now());

        BDDMockito.when(dietService.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(actualDiet));

        webTestClient.get()
                .uri(DIET_URI + "/" + actualId)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Diet.class)
                .value(dietResponse -> {
                    Assertions.assertEquals(actualId, dietResponse.getId());
                    Assertions.assertEquals(actualUser, dietResponse.getUser());
                    Assertions.assertEquals(actualNutrition, dietResponse.getNutrition());
                });
    }

    @Test
    public void findAllDietsByUsername_test() {
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Nutrition actualNutrition1 = new Nutrition("pizza", 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Nutrition actualNutrition2 = new Nutrition("burger", 500.3, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet1 = new Diet("1", actualNutrition1, actualUser, 4.0, 567.0, LocalDateTime.now());
        Diet actualDiet2 = new Diet("2", actualNutrition2, actualUser, 3.0, 1500.3, LocalDateTime.now());

        BDDMockito.when(dietService.findAllByUsername(ArgumentMatchers.anyString())).thenReturn(Flux.just(actualDiet1, actualDiet2));

        webTestClient.get()
                .uri(DIET_URI + "/user/" + actualUsername)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Diet.class)
                .value(dietResponse -> {
                    Assertions.assertEquals(2, dietResponse.size());
                    Assertions.assertEquals(actualNutrition1, dietResponse.get(0).getNutrition());
                    Assertions.assertEquals(actualNutrition2, dietResponse.get(1).getNutrition());
                });
    }

    @Test
    public void findAllDietsByNutritionName_test() {
        String actualName = "pizza";
        User actualUser1 = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                    LocalDate.of(2003, 8, 8));
        User actualUser2 = new User("sakisaki", "さき", "かわさき", 58.7, 1.66, "FEMALE", "lovetaishi@gmail.com",
                                    LocalDate.of(2003, 4, 23));
        Nutrition actualNutrition = new Nutrition(actualName, 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet1 = new Diet("1", actualNutrition, actualUser1, 4.0, 567.0, LocalDateTime.now());
        Diet actualDiet2 = new Diet("2", actualNutrition, actualUser2, 3.0, 1500.3, LocalDateTime.now());

        BDDMockito.when(dietService.findAllByNutritionName(ArgumentMatchers.anyString())).thenReturn(Flux.just(actualDiet1, actualDiet2));

        webTestClient.get()
                .uri(DIET_URI + "/nutrition/" + actualName)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Diet.class)
                .value(dietResponse -> {
                    Assertions.assertEquals(2, dietResponse.size());
                    Assertions.assertEquals(actualNutrition, dietResponse.get(0).getNutrition());
                    Assertions.assertEquals(actualNutrition, dietResponse.get(1).getNutrition());
                });
    }

    @Test
    public void deleteAllDiets_test() {
        BDDMockito.when(dietService.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(DIET_URI)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .value(Assertions::assertNull);
    }

    @Test
    public void deleteDietById_test() {
        String actualId = "1";
        BDDMockito.when(dietService.deleteById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(DIET_URI + "/" + actualId)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .value(Assertions::assertNull);
    }

    @Test
    public void deleteDietByUsername_test() {
        String actualUsername = "eightman";
        BDDMockito.when(dietService.deleteByUsername(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(DIET_URI + "/user/" + actualUsername)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .value(Assertions::assertNull);
    }
}
