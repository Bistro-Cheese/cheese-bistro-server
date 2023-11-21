package com.ooadprojectserver.restaurantmanagement.service.ingredient;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.ingredient.IngredientRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.repository.ingredient.IngredientRepository;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@Service
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private static final Logger logger = LoggerFactory.getLogger(IngredientServiceImpl.class);
    private final IngredientRepository ingredientRepository;
    private final InventoryRepository  inventoryRepository;
    private final UserDetailService userDetailService;



    @Override
    public void create(IngredientRequest req) {
        ingredientRepository.findBySupplierAndName(req.getSupplier(), req.getName()).ifPresent(ingredient -> {
            throw new CustomException(APIStatus.INGREDIENT_ALREADY_EXISTED);
        });

        Ingredient newIngredient = copyProperties(req, Ingredient.class);

        newIngredient.setCommonCreate(userDetailService.getIdLogin());

        Inventory inventory = new Inventory();
        inventory.setIngredient(newIngredient);
        inventory.setTotalQuantity(0.0);

        inventory.setCommonCreate(userDetailService.getIdLogin());

        ingredientRepository.save(newIngredient);
        inventoryRepository.save(inventory);
    }

    @Override
    public void update(Long id, IngredientRequest req) {
        Ingredient ingredient = getIngredient(id);

        if (ingredient.getName().equals(req.getName()) && ingredient.getSupplier().equals(req.getSupplier())) {
            throw new CustomException(APIStatus.INGREDIENT_ALREADY_EXISTED);
        }

        ingredient.setName(req.getName());
        ingredient.setSupplier(req.getSupplier());
        ingredient.setIngredientType(req.getIngredientType());
        ingredient.setUnit(req.getUnit());

        ingredient.setCommonUpdate(userDetailService.getIdLogin());

        ingredientRepository.save(ingredient);
    }

    @Override
    public void delete(Long id) {
        Ingredient ingredient = getIngredient(id);
        ingredientRepository.delete(ingredient);
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public Ingredient self(Long id) {
        return getIngredient(id);
    }

    public Ingredient getIngredient(Long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> {
            logger.error("Ingredient not found with id: {}", id);
            return new CustomException(APIStatus.INGREDIENT_NOT_FOUND);
        });
    }
}
