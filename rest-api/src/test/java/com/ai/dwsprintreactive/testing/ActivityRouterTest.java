package com.ai.dwsprintreactive.testing;

import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.model.Exercise;
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

import static com.ai.dwsprintreactive.rest.RestURIs.ACTIVITY_URI;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Router.class, ActivityHandler.class, DietHandler.class})
@WebFluxTest
public class ActivityRouterTest {

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

    @BeforeAll // Activate BlockHound
    public static void setUp(){
        BlockHound.install();
    }

    @BeforeEach
    public void initialize() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void findAllActivities_test() {
        User actualUser = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser, 40, 56.69, LocalDateTime.now());
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser, 35, 553.3, LocalDateTime.now());
        Activity actualActivity3 = new Activity("3", actualExercise, actualUser, 5, 42.69, LocalDateTime.now());

        BDDMockito.when(activityService.findAll()).thenReturn(Flux.just(actualActivity1, actualActivity2, actualActivity3));

        webTestClient.get()
                .uri(ACTIVITY_URI)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Activity.class)
                .value(activityResponse -> {
                    Assertions.assertEquals(3, activityResponse.size());
                    Assertions.assertEquals(actualUser, activityResponse.get(0).getUser());
                    Assertions.assertEquals(actualUser, activityResponse.get(1).getUser());
                    Assertions.assertEquals(actualUser, activityResponse.get(2).getUser());
                    Assertions.assertEquals(actualExercise, activityResponse.get(0).getExercise());
                    Assertions.assertEquals(actualExercise, activityResponse.get(1).getExercise());
                    Assertions.assertEquals(actualExercise, activityResponse.get(2).getExercise());
                });
    }

    @Test // Make sure BlockHound works
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
    public void findActivityById_test() {
        String actualId = "1";
        User actualUser = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("07040", 1.3, "inactivity quiet/light", "soccer, standing quietly, standing in a line");
        Activity actualActivity = new Activity(actualId, actualExercise, actualUser, 40, 56.69, LocalDateTime.now());

        BDDMockito.when(activityService.findById(ArgumentMatchers.anyString())).thenReturn(Mono.just(actualActivity));

        webTestClient.get()
                .uri(ACTIVITY_URI + "/" + actualId)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Activity.class)
                .value(activityResponse -> {
                    Assertions.assertEquals(actualId, activityResponse.getId());
                    Assertions.assertEquals(actualUser, activityResponse.getUser());
                    Assertions.assertEquals(actualExercise, activityResponse.getExercise());
                });
    }

    @Test
    public void findAllActivitiesByUsername_test() {
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise1 = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Exercise actualExercise2 = new Exercise("07040", 1.3, "inactivity quiet/light", "soccer, standing quietly, standing in a line");
        Activity actualActivity1 = new Activity("1", actualExercise1, actualUser, 40, 56.69, LocalDateTime.now());
        Activity actualActivity2 = new Activity("2", actualExercise2, actualUser, 35, 553.3, LocalDateTime.now());

        BDDMockito.when(activityService.findAllByUsername(ArgumentMatchers.anyString())).thenReturn(Flux.just(actualActivity1, actualActivity2));

        webTestClient.get()
                .uri(ACTIVITY_URI + "/user/" + actualUsername)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Activity.class)
                .value(activityResponse -> {
                    Assertions.assertEquals(2, activityResponse.size());
                    Assertions.assertEquals(actualExercise1, activityResponse.get(0).getExercise());
                    Assertions.assertEquals(actualExercise2, activityResponse.get(1).getExercise());
                });
    }

    @Test
    public void findAllActivitiesByExerciseCompcode_test() {
        String actualCompcode = "12120";
        User actualUser1 = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                    LocalDate.of(2003, 8, 8));
        User actualUser2 = new User("sakisaki", "さき", "かわさき", 58.7, 1.66, "FEMALE", "lovetaishi@gmail.com",
                                    LocalDate.of(2003, 4, 23));
        Exercise actualExercise = new Exercise(actualCompcode, 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser1, 40, 56.69, LocalDateTime.now());
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser2, 35, 553.3, LocalDateTime.now());

        BDDMockito.when(activityService.findAllByExerciseCompcode(ArgumentMatchers.anyString())).thenReturn(Flux.just(actualActivity1, actualActivity2));

        webTestClient.get()
                .uri(ACTIVITY_URI + "/exercise/" + actualCompcode)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Activity.class)
                .value(activityResponse -> {
                    Assertions.assertEquals(2, activityResponse.size());
                    Assertions.assertEquals(actualExercise, activityResponse.get(0).getExercise());
                    Assertions.assertEquals(actualExercise, activityResponse.get(1).getExercise());
                });
    }

    @Test
    public void deleteAllActivities_test() {
       BDDMockito.when(activityService.deleteAll()).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(ACTIVITY_URI)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .value(Assertions::assertNull);
    }

    @Test
    public void deleteActivityById_test() {
        String actualId = "1";
        BDDMockito.when(activityService.deleteById(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(ACTIVITY_URI + "/" + actualId)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .value(Assertions::assertNull);
    }

    @Test
    public void deleteActivityByUsername_test() {
        String actualUsername = "eightman";
        BDDMockito.when(activityService.deleteByUsername(ArgumentMatchers.anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(ACTIVITY_URI + "/user/" + actualUsername)
                .accept(json)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class)
                .value(Assertions::assertNull);
    }
}
