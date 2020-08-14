package com.ai.dwsprintreactive.repository.custom;


import com.ai.dwsprintreactive.config.DietMapper;
import com.ai.dwsprintreactive.model.Diet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DietCustomRepositoryImpl implements DietCustomRepository {

    private final @NotNull DatabaseClient client;

    @Override
    public Mono<Diet> get(Integer id) {
        String query = "SELECT *" +
                       " FROM diet_fact" +
                       " INNER JOIN nutrition_dim nd on diet_fact.nutrition_key = nd.nutrition_id" +
                       " INNER JOIN user_dim ud on diet_fact.user_key = ud.user_id" +
                       " INNER JOIN time_dim td on diet_fact.time_key = td.time_id" +
                       " INNER JOIN date_dim dd on diet_fact.date_key = dd.date_id" +
                       " WHERE diet_fact.diet_id = :id";
        DietMapper mapper = new DietMapper();

        return client.execute(query)
                .bind("id", id)
                .map(mapper)
                .one();
    }

    @Override
    public Flux<Diet> all() {
        String query = "SELECT *" +
                       " FROM diet_fact" +
                       " INNER JOIN nutrition_dim nd on diet_fact.nutrition_key = nd.nutrition_id" +
                       " INNER JOIN user_dim ud on diet_fact.user_key = ud.user_id" +
                       " INNER JOIN time_dim td on diet_fact.time_key = td.time_id" +
                       " INNER JOIN date_dim dd on diet_fact.date_key = dd.date_id" +
                       " ORDER BY diet_id";
        DietMapper mapper = new DietMapper();

        return client.execute(query)
                        .map(mapper)
                        .all();
    }

    @Override //TODO implement me
    public Flux<Diet> findAllDietByDay(Integer dayKey) {

        return null;
    }

    @Override
    public Mono<Diet> save(UUID uuid, Integer nutritionKey, Integer userKey, Double servingQuantity, LocalDateTime createdAt) {
        String timeString = createdAt.toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"))
                .replace(":", "")
                .replaceFirst("^0+(?!$)", "");
        Integer timeKey = Integer.parseInt(timeString);

        String dateString = createdAt.toLocalDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .replace("-", "")
                .replaceFirst("^0+(?!$)", "");
        Integer dateKey = Integer.parseInt(dateString);

        String query = "INSERT INTO diet_fact (diet_uuid, nutrition_key, user_key, time_key, date_key, serving_quantity, calories_consumed, diet_created_at)" +
                       " SELECT :diet_uuid, :nutrition_key, :user_key, :time_key, :date_key, :serving_quantity, :serving_quantity * p.calories, :diet_created_at" +
                       " FROM (SELECT calories FROM nutrition_dim WHERE nutrition_id = :nutrition_key) p";

        return client.execute(query)
                .bind("diet_uuid", uuid)
                .bind("nutrition_key", nutritionKey)
                .bind("user_key", userKey)
                .bind("time_key", timeKey)
                .bind("date_key", dateKey)
                .bind("serving_quantity", servingQuantity)
                .bind("diet_created_at", createdAt)
                .as(Diet.class)
                .fetch()
                .one();
    }
}
