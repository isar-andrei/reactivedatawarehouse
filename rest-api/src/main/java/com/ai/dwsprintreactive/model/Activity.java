package com.ai.dwsprintreactive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@Table("activity_fact")
public class Activity {

    @Id
    @Column("activity_id") private Integer id;

    @Column("activity_uuid") private UUID uuid;

    @Column("activity_exercise_key") private Exercise exercise;

    @Column("activity_user_key") private User user;

    @Column("activity_time_key") private Time time;

    @Column("activity_date_key") private Date date;

    private Integer duration;

    @Column("calories_burned") private Double caloriesBurned;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column("activity_created_at") private LocalDateTime createdAt;
}
