package com.ooadprojectserver.restaurantmanagement.service.food.builder;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodCreateRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.repository.food.CategoryRepository;

public class FoodDirector {
    private final FoodBuilder foodBuilder;
    private final CategoryRepository categoryRepository;

    public FoodDirector(FoodBuilder foodBuilder, CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.foodBuilder = foodBuilder;
    }

    public void construct(FoodCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );

        foodBuilder.reset()
                .buildName(request.getName())
                .buildDescription(request.getDescription())
                .buildProductImage(request.getProductImage())
                .buildCategory(category)
                .buildStatus(request.getStatus())
                .buildPrice(request.getPrice());
    }

    public Food getFood() {
        return foodBuilder.getFood();
    }
}
