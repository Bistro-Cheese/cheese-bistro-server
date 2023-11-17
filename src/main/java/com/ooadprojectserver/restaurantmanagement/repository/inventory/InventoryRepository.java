package com.ooadprojectserver.restaurantmanagement.repository.inventory;

import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
//    @Query("select i from Inventory i where i.ingredient.id = ?1")
//    Inventory findByIngredient_Id(Long id);
//
//    @Transactional
//    @Modifying
//    @Query("delete from Inventory i where i.ingredient = ?1")
//    void deleteByIngredient(Ingredient ingredient);
}
