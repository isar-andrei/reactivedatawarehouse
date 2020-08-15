package com.ai.dwsprintreactive.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("date_dim")
public class Date {

    @Id @Column("date_id") private Integer id;

    private LocalDate date;

    private BigInteger epoch;

    private Integer day;

    @Column("day_name") private String dayName;

    private Integer month;

    @Column("month_name") private String monthName;

    private Integer year;

    private Boolean weekend;
}
