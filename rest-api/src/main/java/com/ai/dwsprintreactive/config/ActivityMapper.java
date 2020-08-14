package com.ai.dwsprintreactive.config;

import com.ai.dwsprintreactive.model.*;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

public class ActivityMapper implements BiFunction<Row, RowMetadata, Activity> {

    @Override
    public Activity apply(Row row, RowMetadata rowMetadata) {
        Integer activityId = row.get("activity_id", Integer.class);
        UUID activityUuid = row.get("activity_uuid", UUID.class);
        Integer duration = row.get("duration", Integer.class);
        Double caloriesBurned = row.get("calories_burned", Double.class);
        LocalDateTime activityCreatedAt = row.get("activity_created_at", LocalDateTime.class);

        Integer exerciseId = row.get("exercise_id", Integer.class);
        UUID exerciseUuid = row.get("exercise_uuid", UUID.class);
        String compcode = row.get("compcode", String.class);
        Double met = row.get("met", Double.class);
        String category = row.get("category", String.class);
        String description = row.get("description", String.class);
        Exercise exerciseEntity = new Exercise(exerciseId, exerciseUuid, compcode, met, category, description);

        Integer userId = row.get("user_id", Integer.class);
        UUID userUUid = row.get("user_uuid", UUID.class);
        String firstName = row.get("first_name", String.class);
        String lastName = row.get("last_name", String.class);
        Double weight = row.get("weight", Double.class);
        Double height = row.get("height", Double.class);
        String gender = row.get("gender", String.class);
        LocalDate birthday = row.get("birthday", LocalDate.class);
        String username = row.get("username", String.class);
        String email = row.get("email", String.class);
        LocalDateTime userCreatedAt = row.get("user_created_at", LocalDateTime.class);
        LocalDateTime updatedAt = row.get("updated_at", LocalDateTime.class);
        User userEntity = new User(userId, userUUid, firstName, lastName, weight, height, gender, birthday,
                                   username, email, userCreatedAt, updatedAt);

        Integer timeId = row.get("time_id", Integer.class);
        String time = row.get("time", String.class);
        String hour = row.get("hour", String.class);
        String minute = row.get("minute", String.class);
        String period = row.get("period", String.class);
        Time timeEntity = new Time(timeId, time, hour, minute, period);

        Integer dateId = row.get("date_id", Integer.class);
        LocalDate date = row.get("date", LocalDate.class);
        BigInteger epoch = row.get("epoch", BigInteger.class);
        Integer day = row.get("day", Integer.class);
        String dayName = row.get("day_name", String.class);
        Integer month = row.get("month", Integer.class);
        String monthName = row.get("month_name", String.class);
        Integer year = row.get("year", Integer.class);
        Boolean weekend = row.get("weekend", Boolean.class);
        Date dateEntity = new Date(dateId, date, epoch, day, dayName, month, monthName, year, weekend);

        return new Activity(activityId, activityUuid, exerciseEntity, userEntity, timeEntity, dateEntity, duration, caloriesBurned, activityCreatedAt);
    }
}
