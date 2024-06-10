package com.ooadprojectserver.restaurantmanagement.service.food.builder;

import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;

import java.math.BigDecimal;

public class ConcreteFoodBuilder implements FoodBuilder {
    private Food food;

    @Override
    public FoodBuilder reset() {
        this.food = new Food();
        return this;
    }

    @Override
    public FoodBuilder buildName(String name) {
        this.food.setName(name);
        return this;
    }

    @Override
    public FoodBuilder buildCategory(Category category) {
        this.food.setCategory(category);
        return this;
    }

    @Override
    public FoodBuilder buildDescription(String description) {
        this.food.setDescription(description);
        return this;
    }

    @Override
    public FoodBuilder buildProductImage(String productImage) {
        this.food.setProductImage(productImage);
        return this;
    }

    @Override
    public FoodBuilder buildPrice(BigDecimal price) {
        this.food.setPrice(price);
        return this;
    }

    @Override
    public FoodBuilder buildStatus(Integer status) {
        this.food.setStatus(status);
        return this;
    }

    @Override
    public Food getFood() {
        return this.food;
    }
}
