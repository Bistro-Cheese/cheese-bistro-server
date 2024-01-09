package com.ooadprojectserver.restaurantmanagement.repository.inventory;

import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.repository.custom.InventoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID>, InventoryRepositoryCustom {
    Optional<Inventory> findByIngredient(Ingredient ingredient);

    @Transactional
    @Modifying
    @Query("delete from Inventory i where i.ingredient = ?1")
    void deleteByIngredient(Ingredient ingredient);

}
