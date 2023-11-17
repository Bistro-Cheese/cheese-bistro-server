package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;

import java.util.List;
import java.util.UUID;

public interface FoodService {

    //Implement Product
    List<Food> getAllFoods();

    void createFood(FoodRequest request);

    void deleteFood(UUID foodId);

    void updateFood(UUID foodId, FoodRequest updatingFood);

    Food getDetailFood(UUID foodId);

    PagingResponse searchFood(SearchRequest searchRequest);
}
