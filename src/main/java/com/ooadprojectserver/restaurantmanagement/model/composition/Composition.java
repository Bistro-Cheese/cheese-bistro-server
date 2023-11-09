package com.ooadprojectserver.restaurantmanagement.model.composition;

import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "composition")
public class Composition {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;

    @ManyToOne
    @JoinColumn(name = "ingre_id")
    private Ingredient ingredient;

    @Column(name = "portion", nullable = false)
    private Integer portion;
}
