package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface FoodService {

    //Implement Product
    List<Food> getAllFoods(HttpServletRequest request);

    void createFood(FoodRequest request);

    void deleteFood(UUID foodId);

    void updateFood(UUID foodId, FoodRequest updatingFood);

    Food getDetailFood(UUID foodId);

    PagingResponseModel searchFood(String category, String searchKey, BigDecimal minPrice, BigDecimal maxPrice,
                                                     Integer sortCase, Boolean isAscSort, Integer pageNumber,
                                                     Integer pageSize);
}
