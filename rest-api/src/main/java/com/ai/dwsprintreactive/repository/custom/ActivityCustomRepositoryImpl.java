package com.ai.dwsprintreactive.repository.custom;

import com.ai.dwsprintreactive.config.ActivityMapper;
import com.ai.dwsprintreactive.model.Activity;
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
public class ActivityCustomRepositoryImpl implements ActivityCustomRepository {

    private final @NotNull DatabaseClient client;

    @Override
    public Mono<Activity> get(Integer id) {
        String query = "SELECT *" +
                       " FROM activity_fact" +
                       " INNER JOIN exercise_dim ed on activity_fact.activity_exercise_key = ed.exercise_id" +
                       " INNER JOIN user_dim ud on activity_fact.activity_user_key = ud.user_id" +
                       " INNER JOIN time_dim td on activity_fact.activity_time_key = td.time_id" +
                       " INNER JOIN date_dim dd on activity_fact.activity_date_key = dd.date_id" +
                       " WHERE activity_id = :id";

        ActivityMapper mapper = new ActivityMapper();

        return client.execute(query)
                .bind("id", id)
                .map(mapper)
                .one();
    }

    @Override
    public Flux<Activity> all() {
        String query = "SELECT *" +
                       " FROM activity_fact" +
                       " INNER JOIN exercise_dim ed on activity_fact.activity_exercise_key = ed.exercise_id" +
                       " INNER JOIN user_dim ud on activity_fact.activity_user_key = ud.user_id" +
                       " INNER JOIN time_dim td on activity_fact.activity_time_key = td.time_id" +
                       " INNER JOIN date_dim dd on activity_fact.activity_date_key = dd.date_id" +
                       " ORDER BY activity_id";
        ActivityMapper mapper = new ActivityMapper();

        return client.execute(query)
                .map(mapper)
                .all();
    }

    @Override
    public Mono<Activity> save(UUID uuid, Integer exerciseKey, Integer userKey, Integer duration, LocalDateTime createdAt) {
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

        String query = "INSERT INTO activity_fact (activity_uuid, activity_exercise_key, activity_user_key, activity_time_key, activity_date_key, duration, calories_burned, activity_created_at)" +
                       " SELECT :activity_uuid, :exercise_key, :user_key, :time_key, :date_key, :duration, ed.met * 3.5 * ud.weight / 200 * :duration, :activity_created_at" +
                       " FROM exercise_dim ed" +
                       " JOIN user_dim ud ON ed.exercise_id = :exercise_key AND ud.user_id = :user_key";

        return client.execute(query)
                .bind("activity_uuid", uuid)
                .bind("exercise_key", exerciseKey)
                .bind("user_key", userKey)
                .bind("time_key", timeKey)
                .bind("date_key", dateKey)
                .bind("duration", duration)
                .bind("activity_created_at", createdAt)
                .as(Activity.class)
                .fetch()
                .one();
    }
}
