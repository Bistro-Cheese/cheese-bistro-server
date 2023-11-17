package com.ooadprojectserver.restaurantmanagement.service.ingredient;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.IngredientRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.inventory.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.repository.food.IngredientRepository;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    private final InventoryRepository inventoryRepository;

//    public List<Ingredient> getAllIngredients() {
//        List<Ingredient> ingredientList = ingredientRepository.findAll();
//        Comparator<Ingredient> comparator = Comparator.comparing(Ingredient::getIngredientType);
//        ingredientList.sort(comparator);
//        return ingredientRepository.findAll();
//    }
//
//    public void createIngredient(IngredientRequest request) {
//        Optional<Ingredient> optionalIngredient = ingredientRepository.findByName(request.getName());
//        if (optionalIngredient.isEmpty()) {
//            ingredientRepository.save(
//                    Ingredient.builder()
//                            .name(request.getName())
//                            .ingredientType(request.getType())
//                            .build()
//            );
//        } else {
//            throw new CustomException(APIStatus.INGREDIENT_ALREADY_EXISTED);
//        }
//    }
//
//    public void deleteIngredient(Long ingredient_id) {
//        Ingredient ingredient = ingredientRepository.findById(ingredient_id).orElseThrow(
//                () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
//        );
//        inventoryRepository.deleteByIngredient(ingredient);
//        compositionRepository.deleteByIngredient(ingredient);
//        ingredientRepository.delete(ingredient);
//    }
//
//    public void updateIngredient(Long ingredient_id, IngredientRequest request) {
//        Ingredient ingredient = ingredientRepository.findById(ingredient_id).orElseThrow(
//                () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
//        );
//        ingredientRepository.updateIngredient(request.getName(), request.getType(), ingredient_id);
//    }
}
