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
public class Activity {

    private Integer exerciseKey;

    private Integer userKey;

    private Integer duration;

    private LocalDateTime createdAt;
}
