package com.ai.metrics.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nutrition {

    private UUID id;

    private String name;

    private Double calories;

    private Double fat;

    private Double saturatedFat;

    private Double carbohydrates;

    private Double fiber;

    private Double sugar;

    private Double protein;

    private Double sodium;
}

