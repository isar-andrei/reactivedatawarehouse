package com.ai.etl.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diet {

    private Integer nutritionKey;

    private Integer userKey;

    private Integer servingQuantity;

    private LocalDateTime createdAt;

}
