package com.ooadprojectserver.restaurantmanagement.model.inventory;

import com.ooadprojectserver.restaurantmanagement.model.CommonEntity;
import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory extends CommonEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @NotNull
    @Column(name = "total_quan", nullable = false, length = 45)
    private Double totalQuantity;

    public void importInventory(Double quantity) {
        this.totalQuantity += quantity;
    }

    public void exportInventory(Double quantity) {
        this.totalQuantity -= quantity;
    }

    public Boolean isEnough(Double quantity) {
        return this.totalQuantity >= quantity;
    }
}