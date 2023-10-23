package com.ooadprojectserver.restaurantmanagement.service.inventory;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.ImportInventoryRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.repository.food.IngredientRepository;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final IngredientRepository ingredientRepository;

    public List<Inventory> getInventory() {
        return inventoryRepository.findAll();
    }

    public void importIngredient(
            ImportInventoryRequest request,
            Long ingredientId
    ) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElseThrow(
                () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
        );
        Inventory inventory = inventoryRepository.findByIngredient_Id(ingredientId);
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
            inventoryRepository.save(inventory);
            return;
        }
        inventoryRepository.save(
                Inventory.builder()
                        .ingredient(ingredient)
                        .quantity(request.getQuantity())
                        .build()
        );
    }
}
