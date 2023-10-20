package com.ooadprojectserver.restaurantmanagement.model.composition.ingredient;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ingredient")
public class Ingredient implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String name;

    @Column(name = "type", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Integer ingredientType;
}
