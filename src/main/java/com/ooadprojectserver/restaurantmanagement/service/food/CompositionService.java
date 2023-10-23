package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.composition.CompositionRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.composition.CompositionResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.composition.FoodInfoResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.composition.IngredientInfoResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import com.ooadprojectserver.restaurantmanagement.repository.food.CompositionRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompositionService {
    private final FoodRepository foodRepository;
    private final IngredientRepository ingredientRepository;
    private final CompositionRepository compositionRepository;

    public List<CompositionResponse> getAllCompositions() {
        List<Food> foodList = foodRepository.findAll();
        List<CompositionResponse> compositionResponseList = new ArrayList<>();
        for (Food food : foodList) {
            List<IngredientInfoResponse> ingredientInfoResponseList = new ArrayList<>();
            FoodInfoResponse foodInfoResponse = FoodInfoResponse.builder()
                    .id(food.getId())
                    .name(food.getName())
                    .build();
            List<Composition> compositionList = compositionRepository.findByFood(food.getId());
            for (Composition composition : compositionList) {
                ingredientInfoResponseList.add(
                        IngredientInfoResponse.builder()
                                .id(composition.getIngredient().getId())
                                .name(composition.getIngredient().getName())
                                .portion(composition.getPortion())
                                .build()
                );
            }
            compositionResponseList.add(
                    CompositionResponse.builder()
                            .food(foodInfoResponse)
                            .ingredients(ingredientInfoResponseList)
                            .build()
            );
        }
        return compositionResponseList;
    }

    public void createComposition(UUID foodId, CompositionRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        request.getIngredients().forEach(
                ingredient -> {
                    Ingredient existedIngredient = ingredientRepository.findById(ingredient.getIngredientId()).orElseThrow(
                            () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
                    );
                    compositionRepository.save(
                            Composition.builder()
                                    .food(food)
                                    .ingredient(existedIngredient)
                                    .portion(ingredient.getPortion())
                                    .build()
                    );
                }
        );
    }

    public void deleteComposition(UUID food_id) {
        Food food = foodRepository.findById(food_id).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        compositionRepository.deleteByFood(food);
    }

    public void updateComposition(UUID foodId, CompositionRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        request.getIngredients().forEach(
                ingredient -> {
                    Ingredient existedIngredient = ingredientRepository.findById(ingredient.getIngredientId()).orElseThrow(
                            () -> new CustomException(APIStatus.INGREDIENT_NOT_FOUND)
                    );
                    Composition composition = compositionRepository.findComposition(foodId, ingredient.getIngredientId());
                    if (composition == null) {
                        compositionRepository.save(
                                Composition.builder()
                                        .food(food)
                                        .ingredient(existedIngredient)
                                        .portion(ingredient.getPortion())
                                        .build()
                        );
                    } else {
                        compositionRepository.updateIngredientAndPortionById(
                                existedIngredient,
                                ingredient.getPortion(),
                                composition.getId()
                        );
                    }
                }
        );
    }
}