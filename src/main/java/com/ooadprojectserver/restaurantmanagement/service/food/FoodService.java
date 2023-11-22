package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.food.FoodResponse;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface FoodService {

    //Implement Product
    List<Food> getAllFoods();

    void createFood(FoodCreateRequest request);

    void deleteFood(UUID foodId);

    void updateFood(UUID foodId, FoodCreateRequest updatingFood);

    Food getDetailFood(UUID foodId);

    Page<FoodResponse> search(FoodSearchRequest request, PageInfo pageInfo);
}
