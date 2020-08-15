package com.ai.dwsprintreactive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("diet_fact")
public class Diet {

    @Id @Column("diet_id") private Integer id;

    @Column("diet_uuid") private UUID uuid;

    @Column("nutrition_key") private Nutrition nutrition;

    @Column("user_key") private User user;

    @Column("time_key") private Time time;

    @Column("date_key") private Date date;

    @Column("serving_quantity") private Double servingQuantity;

    @Column("calories_consumed") private Double caloriesConsumed;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column("diet_created_at") private LocalDateTime createdAt;
}
