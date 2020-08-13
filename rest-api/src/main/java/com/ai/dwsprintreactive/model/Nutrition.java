package com.ai.dwsprintreactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("nutrition_dim")
public class Nutrition {

    @Id @Column("nutrition_id") private Integer id;

    @Column("nutrition_uuid") private UUID uuid;

    private String name;

    private Double calories;

    private Double fat;

    @Column("saturated_fat") private Double saturatedFat;

    private Double carbohydrates;

    private Double fiber;

    private Double sugar;

    private Double protein;

    private Double sodium;
}
