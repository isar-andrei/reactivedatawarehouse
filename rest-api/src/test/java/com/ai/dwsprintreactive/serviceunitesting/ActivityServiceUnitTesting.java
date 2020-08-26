package com.ai.dwsprintreactive.serviceunitesting;


import com.ai.dwsprintreactive.model.Activity;
import com.ai.dwsprintreactive.model.Exercise;
import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.repository.ActivityRepository;
import com.ai.dwsprintreactive.service.impl.ActivityServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ActivityServiceUnitTesting {

    @InjectMocks
    private ActivityServiceImpl service;

    @Mock
    private ActivityRepository repository;

    @BeforeAll
    public static void setUpClass() {
        BlockHound.install();
    }

    @Test
    @Order(1)
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
    public void findById_test() {
        String actualId = "1";
        User actualUser = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                    LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("07040", 1.3, "inactivity quiet/light", "soccer, standing quietly, standing in a line");
        Activity actualActivity1 = new Activity(actualId, actualExercise, actualUser, 40, 56.69, LocalDateTime.now());

        BDDMockito.when(repository.findById(actualId))
                .thenReturn(Mono.just(actualActivity1));

        StepVerifier.create(service.findById(actualId))
                .expectSubscription()
                .expectNext(actualActivity1)
                .verifyComplete();
    }

    @Test
    public void findAllByUsername_test() {
        String actualUsername1 = "eightman";
        User actualUser = new User(actualUsername1, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise1 = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Exercise actualExercise2 = new Exercise("07040", 1.3, "inactivity quiet/light", "soccer, standing quietly, standing in a line");
        Activity actualActivity1 = new Activity("1", actualExercise1, actualUser, 40, 56.69, LocalDateTime.now());
        Activity actualActivity2 = new Activity("2", actualExercise2, actualUser, 35, 553.3, LocalDateTime.now());

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualActivity1, actualActivity2));

        StepVerifier.create(service.findAllByUsername(actualUsername1))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findAllByExerciseCompcode_test() {
        String actualCompcode = "12120";
        User actualUser1 = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        User actualUser2 = new User("sakisaki", "さき", "かわさき", 58.7, 1.66, "FEMALE", "lovetaishi@gmail.com",
                                                  LocalDate.of(2003, 4, 23));
        Exercise actualExercise = new Exercise(actualCompcode, 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser1, 40, 56.69, LocalDateTime.now());
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser2, 35, 553.3, LocalDateTime.now());

        BDDMockito.when(repository.findAllByExerciseCompcode(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualActivity1, actualActivity2));

        StepVerifier.create(service.findAllByExerciseCompcode(actualCompcode))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void sumCaloriesOnCurrentDay_test() {
        LocalDateTime expectedNow = LocalDateTime.now();
        Double expectedCaloriesBurned1 = 56.69;
        Double expectedCaloriesBurned2 = 553.3;
        Double expectedCaloriesBurned3 = 42.69;
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                                      LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser, 40, expectedCaloriesBurned1, expectedNow);
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser, 35, expectedCaloriesBurned2, expectedNow);
        Activity actualActivity3 = new Activity("3", actualExercise, actualUser, 5, expectedCaloriesBurned3, expectedNow.minusDays(1));

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualActivity1, actualActivity2, actualActivity3));

        StepVerifier.create(service.sumCaloriesOnCurrentDay(actualUsername))
                .expectSubscription()
                .expectNext(expectedCaloriesBurned1 + expectedCaloriesBurned2)
                .verifyComplete();
    }

    @Test
    public void avgCaloriesOnCurrentWeek_test() {
        LocalDateTime expectedNow = LocalDateTime.now();
        LocalDate starting = expectedNow.toLocalDate().with(DayOfWeek.MONDAY);
        LocalDate ending = expectedNow.toLocalDate().with(DayOfWeek.SUNDAY);
        Double expectedCaloriesBurned1 = 56.69;
        Double expectedCaloriesBurned2 = 553.3;
        Double expectedCaloriesBurned3 = 42.69;
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser, 40, expectedCaloriesBurned1, starting.atStartOfDay());
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser, 35, expectedCaloriesBurned2, ending.atStartOfDay());
        Activity actualActivity3 = new Activity("3", actualExercise, actualUser, 5, expectedCaloriesBurned3, expectedNow.plusDays(8));

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualActivity1, actualActivity2, actualActivity3));

        StepVerifier.create(service.avgCaloriesOnCurrentWeek(actualUsername))
                .expectSubscription()
                .expectNext((expectedCaloriesBurned1 + expectedCaloriesBurned2) / 2)
                .verifyComplete();
    }

    @Test
    public void avgCaloriesBetweenDates_test() {
        LocalDateTime expectedNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime starting = expectedNow.minusDays(1);
        LocalDateTime ending = expectedNow.plusDays(1);
        String startingString = starting.format(formatter);
        String endingString = ending.format(formatter);
        Double expectedCaloriesBurned1 = 56.69;
        Double expectedCaloriesBurned2 = 553.3;
        Double expectedCaloriesBurned3 = 42.69;
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser, 40, expectedCaloriesBurned1, starting.plusSeconds(1));
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser, 35, expectedCaloriesBurned2, ending.minusSeconds(1));
        Activity actualActivity3 = new Activity("3", actualExercise, actualUser, 5, expectedCaloriesBurned3, ending.plusDays(1));

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualActivity1, actualActivity2, actualActivity3));

        StepVerifier.create(service.avgCaloriesBetweenDates(actualUsername, startingString, endingString))
                .expectSubscription()
                .expectNext((expectedCaloriesBurned1 + expectedCaloriesBurned2) / 2)
                .verifyComplete();
    }

    @Test
    public void findAll_test() {
        User actualUser = new User("eightman", "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Exercise actualExercise = new Exercise("12120", 14.5, "running", "running, 10 mph (6 min/mile)");
        Activity actualActivity1 = new Activity("1", actualExercise, actualUser, 40, 56.69, LocalDateTime.now());
        Activity actualActivity2 = new Activity("2", actualExercise, actualUser, 35, 553.3, LocalDateTime.now());
        Activity actualActivity3 = new Activity("3", actualExercise, actualUser, 5, 42.69, LocalDateTime.now());

        BDDMockito.when(repository.findAll())
                .thenReturn(Flux.just(actualActivity1, actualActivity2, actualActivity3));

        StepVerifier.create(service.findAll())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }
}
