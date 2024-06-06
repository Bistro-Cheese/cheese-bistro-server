package com.ooadprojectserver.restaurantmanagement.service.inventory;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventoryCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventorySearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventoryUpdateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.inventory.InventoryResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.repository.ingredient.IngredientRepository;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import com.ooadprojectserver.restaurantmanagement.service.ingredient.IngredientServiceImpl;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final IngredientRepository ingredientRepository;
    private final UserDetailService userDetailService;

    @Override
    public void create(InventoryCreateRequest req) {

        Ingredient ingredient =ingredientRepository.findById(req.getIngredientId()).orElseThrow(
                () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
        );

        inventoryRepository.findByIngredient(ingredient).orElseThrow(
                () -> new CustomException(APIStatus.INVENTORY_ALREADY_EXIST)
        );

        Inventory inventory = new Inventory();
        inventory.setIngredient(ingredient);
        inventory.setTotalQuantity(req.getTotalQuantity());
        inventory.setCommonCreate(userDetailService.getIdLogin());

        inventoryRepository.save(inventory);
    }

    @Override
    public Page<InventoryResponse> search(InventorySearchRequest req, PageInfo pageInfo) {
        return inventoryRepository.search(req, pageInfo);
    }


    @Override
    public void update(UUID id, InventoryUpdateRequest req) {
        Inventory inventory = getInventory(id);

        inventory.setTotalQuantity(req.getTotalQuantity());
        inventory.setCommonUpdate(userDetailService.getIdLogin());
        inventoryRepository.save(inventory);
    }

    @Override
    public void delete(UUID id) {
        Inventory inventory = getInventory(id);
        inventoryRepository.delete(inventory);
    }

    @Override
    public Inventory self(UUID id) {
        return getInventory(id);
    }

    private Inventory getInventory(UUID id) {
        return inventoryRepository.findById(id).orElseThrow(
                () -> new CustomException(APIStatus.INVENTORY_NOT_FOUND)
        );
    }
}
