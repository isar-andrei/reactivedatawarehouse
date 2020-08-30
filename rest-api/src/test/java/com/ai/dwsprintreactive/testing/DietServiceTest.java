package com.ai.dwsprintreactive.testing;

import com.ai.dwsprintreactive.model.Diet;
import com.ai.dwsprintreactive.model.Nutrition;
import com.ai.dwsprintreactive.model.User;
import com.ai.dwsprintreactive.repository.DietRepository;
import com.ai.dwsprintreactive.service.impl.DietServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
public class DietServiceTest {

    @InjectMocks
    private DietServiceImpl service;

    @Mock
    private DietRepository repository;

    @BeforeAll
    public static void setUpClass() {
        BlockHound.install();
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
    public void sumCaloriesOnCurrentDay_test() {
        LocalDateTime expectedNow = LocalDateTime.now();
        Double expectedCaloriesIntake1 = 56.69;
        Double expectedCaloriesIntake2 = 553.3;
        Double expectedCaloriesIntake3 = 42.69;
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Nutrition actualNutrition = new Nutrition("pizza", 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet1 = new Diet("1", actualNutrition, actualUser, 4.0, expectedCaloriesIntake1, expectedNow);
        Diet actualDiet2 = new Diet("2", actualNutrition, actualUser, 3.0, expectedCaloriesIntake2, expectedNow);
        Diet actualDiet3 = new Diet("3", actualNutrition, actualUser, 5.0, expectedCaloriesIntake3, expectedNow.minusDays(1));

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualDiet1, actualDiet2, actualDiet3));

        StepVerifier.create(service.sumCaloriesOnCurrentDay(actualUsername))
                .expectSubscription()
                .expectNext(expectedCaloriesIntake1 + expectedCaloriesIntake2)
                .verifyComplete();
    }

    @Test
    public void avgCaloriesOnCurrentWeek_test() {
        LocalDateTime expectedNow = LocalDateTime.now();
        LocalDate starting = expectedNow.toLocalDate().with(DayOfWeek.MONDAY);
        LocalDate ending = expectedNow.toLocalDate().with(DayOfWeek.SUNDAY);
        Double expectedCaloriesIntake1 = 56.69;
        Double expectedCaloriesIntake2 = 553.3;
        Double expectedCaloriesIntake3 = 42.69;
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Nutrition actualNutrition = new Nutrition("pizza", 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet1 = new Diet("1", actualNutrition, actualUser, 4.0, expectedCaloriesIntake1, starting.atStartOfDay());
        Diet actualDiet2 = new Diet("2", actualNutrition, actualUser, 3.0, expectedCaloriesIntake2, ending.atStartOfDay());
        Diet actualDiet3 = new Diet("3", actualNutrition, actualUser, 5.0, expectedCaloriesIntake3, expectedNow.plusDays(8));

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(actualDiet1, actualDiet2, actualDiet3));

        StepVerifier.create(service.avgCaloriesOnCurrentWeek(actualUsername))
                .expectSubscription()
                .expectNext((expectedCaloriesIntake1 + expectedCaloriesIntake2) / 2)
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
        Double expectedCaloriesIntake1 = 56.69;
        Double expectedCaloriesIntake2 = 553.3;
        Double expectedCaloriesIntake3 = 42.69;
        String actualUsername = "eightman";
        User actualUser = new User(actualUsername, "はちまん", "ひきがや", 62.3, 1.75, "MALE", "sadyahallo@gmail.com",
                                   LocalDate.of(2003, 8, 8));
        Nutrition actualNutrition = new Nutrition("pizza", 300.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Diet actualDiet1 = new Diet("1", actualNutrition, actualUser, 4.0, expectedCaloriesIntake1, starting.plusSeconds(1));
        Diet actualDiet2 = new Diet("2", actualNutrition, actualUser, 3.0, expectedCaloriesIntake2, ending.minusSeconds(1));
        Diet actualDiet3 = new Diet("3", actualNutrition, actualUser, 5.0, expectedCaloriesIntake3, ending.plusDays(1));

        BDDMockito.when(repository.findAllByUsername(ArgumentMatchers.anyString())).thenReturn(Flux.just(actualDiet1, actualDiet2, actualDiet3));

        StepVerifier.create(service.avgCaloriesBetweenDates(actualUsername, startingString, endingString))
                .expectSubscription()
                .expectNext((expectedCaloriesIntake1 + expectedCaloriesIntake2) / 2)
                .verifyComplete();
    }
}
