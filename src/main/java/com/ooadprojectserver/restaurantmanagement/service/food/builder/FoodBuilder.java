package com.ooadprojectserver.restaurantmanagement.service.food.builder;

import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;

import java.math.BigDecimal;

public interface FoodBuilder {
    FoodBuilder reset();
    FoodBuilder buildName(String name);
    FoodBuilder buildCategory(Category category);
    FoodBuilder buildDescription(String description);
    FoodBuilder buildProductImage(String productImage);
    FoodBuilder buildPrice(BigDecimal price);
    FoodBuilder buildStatus(Integer status);
    Food getFood();
}
