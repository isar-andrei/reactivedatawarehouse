package com.ai.dwsprintreactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nutrition {

    @Field("name") @Id private String name;

    private Double calories;

    private Double fat;

    private Double saturatedFat;

    private Double carbohydrates;

    private Double fiber;

    private Double sugar;

    private Double protein;

    private Double sodium;
}
