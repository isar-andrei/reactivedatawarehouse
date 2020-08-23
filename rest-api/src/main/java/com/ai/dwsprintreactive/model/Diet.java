package com.ai.dwsprintreactive.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Document
@CompoundIndex(name = "nuc", def = "{'nutrition.id': 1, 'user.id': 1, 'createdAt' : -1}")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diet {

    @Id private String id;

    private Nutrition nutrition;

    private User user;

    private Double servingQuantity;

    private Double caloriesConsumed;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
