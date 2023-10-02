package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.exception.FoodException;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface FoodService {

    //Implement Product
    List<Food> findAllProducts();

    public Food createProduct(FoodRequest request) throws FoodException;

    public void deleteProduct(UUID foodId) throws FoodException;

    public void updateProduct(UUID foodId, FoodRequest updatingFood) throws FoodException;

    public Food findFoodById(UUID id) throws FoodException;

    public Page<Food> doFilterSearchSortPagingFood(String category, String searchKey, BigDecimal minPrice, BigDecimal maxPrice,
                                                   Integer sortCase, Boolean isAscSort, int pageNumber,
                                                   int pageSize);
}
