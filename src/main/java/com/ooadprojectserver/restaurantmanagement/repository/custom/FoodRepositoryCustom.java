package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.food.FoodResponse;
import org.springframework.data.domain.Page;

public interface FoodRepositoryCustom {
    Page<FoodResponse> search(FoodSearchRequest request, PageInfo pageInfo);
}
