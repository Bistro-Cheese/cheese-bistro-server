package com.ooadprojectserver.restaurantmanagement.model.inventory.ingredient;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @NotNull
    @Column(name = "ingredient_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private IngredientType ingredientType;

    @Size(max = 45)
    @NotNull
    @Column(name = "unit", nullable = false, length = 45)
    private String unit;

    @Size(max = 255)
    @NotNull
    @Column(name = "supplier", nullable = false)
    private String supplier;

}