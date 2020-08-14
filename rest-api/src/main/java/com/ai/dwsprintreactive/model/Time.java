package com.ai.dwsprintreactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@Table("time_dim")
public class Time {

    @Id @Column("time_id") private Integer id;

    private String time;

    private String hour;

    private String minute;

    private String period;
}
