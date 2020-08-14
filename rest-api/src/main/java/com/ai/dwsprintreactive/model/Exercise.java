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
@Table("exercise_dim")
public class Exercise {

    @Id @Column("exercise_id") private Integer id;

    @Column("exercise_uuid") private UUID uuid;

    String compcode;

    Double met;

    String category;

    String description;

}
