package com.ai.etl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePerformed {

    private UUID id;

    private Exercise exercise;

    private User user;

    private Integer time;

    private Integer caloriesBurned;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime date;
}
