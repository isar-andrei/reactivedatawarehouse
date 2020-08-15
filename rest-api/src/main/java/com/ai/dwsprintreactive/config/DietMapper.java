package com.ai.dwsprintreactive.config;

import com.ai.dwsprintreactive.model.*;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiFunction;


public class DietMapper implements BiFunction<Row, RowMetadata, Diet> {

    @Override
    public Diet apply(Row row, RowMetadata rowMetadata) {
        Integer dietId = row.get("diet_id", Integer.class);
        UUID dietUuid = row.get("diet_uuid", UUID.class);
        Double servingQuantity = row.get("serving_quantity", Double.class);
        Double caloriesConsumed = row.get("calories_consumed", Double.class);
        LocalDateTime dietCreatedAt = row.get("diet_created_at", LocalDateTime.class);

        Integer nutritionId = row.get("nutrition_id", Integer.class);
        UUID nutritionUuid = row.get("nutrition_uuid", UUID.class);
        String nutritionName = row.get("name", String.class);
        Double calories = row.get("calories", Double.class);
        Double fat = row.get("fat", Double.class);
        Double saturatedFat = row.get("saturated_fat", Double.class);
        Double carbohydrates = row.get("carbohydrates", Double.class);
        Double fiber = row.get("fiber", Double.class);
        Double sugar = row.get("sugar", Double.class);
        Double protein = row.get("protein", Double.class);
        Double sodium = row.get("sodium", Double.class);
        Nutrition nutritionEntity = new Nutrition(nutritionId, nutritionUuid, nutritionName, calories, fat,
                                                  saturatedFat, carbohydrates, fiber, sugar, protein, sodium);

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

        return new Diet(dietId, dietUuid, nutritionEntity, userEntity, timeEntity, dateEntity, servingQuantity, caloriesConsumed, dietCreatedAt);
    }
}
