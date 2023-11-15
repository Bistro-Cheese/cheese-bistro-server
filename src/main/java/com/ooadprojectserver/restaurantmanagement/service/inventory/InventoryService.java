package com.ooadprojectserver.restaurantmanagement.service.inventory;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.InventoryRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;
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

    public void addIngredientToInventory(InventoryRequest request, Long ingredientId) {
        var quantity = request.getQuantity();
        var ingredient = getIngredient(ingredientId);
        updateIngredient(ingredient, quantity);
    }

    public void returnIngredientToInventory(
           Composition composition, OrderLine orderLine
    ) {
        var quantity = (double) (composition.getPortion() * orderLine.getQuantity());
        var ingredientId = composition.getIngredient().getId();
        var ingredient = getIngredient(ingredientId);
        updateIngredient(ingredient, quantity);
    }

    public void getIngredientFromInventory(Composition composition, OrderLine orderLine) {
        var quantity = (double) (composition.getPortion() * orderLine.getQuantity());
        var ingredientId = composition.getIngredient().getId();
        Inventory inventory = inventoryRepository.findByIngredient_Id(ingredientId);
        if (inventory == null) {
            throw new CustomException(APIStatus.INGREDIENT_NOT_FOUND);
        }
        if (inventory.getQuantity() < quantity) {
            throw new CustomException(APIStatus.INGREDIENT_NOT_ENOUGH);
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    private Ingredient getIngredient(Long ingredientId){
        return ingredientRepository.findById(ingredientId).orElseThrow(
                () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
        );
    }

    private void updateIngredient(Ingredient ingredient, double quantity){
        Inventory inventory = inventoryRepository.findByIngredient_Id(ingredient.getId());
        if (inventory != null) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventoryRepository.save(inventory);
            return;
        }
        var newInventory = Inventory.builder()
                .ingredient(ingredient)
                .quantity(quantity)
                .build();
        inventoryRepository.save(newInventory);
    }

}
