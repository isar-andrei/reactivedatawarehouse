package com.ai.etl.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    private UUID id;

    private Double met;

    private String category;

    private String code;

    private String description;

}
