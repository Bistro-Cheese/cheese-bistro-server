package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.food.FoodResponse;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.service.food.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@EnableCaching
@RequestMapping(APIConstant.FOODS)
public class FoodController {
    private final FoodService foodService;

    //create food
    @PostMapping()
    @CachePut(value = "food", key = "#request")
    public ResponseEntity<MessageResponse> createFood(
            @RequestBody @Valid FoodCreateRequest request
    ) {
        foodService.createFood(request);
        return ResponseEntity.status(CREATED).body(
                new MessageResponse(
                        MessageConstant.CREATE_FOOD_SUCCESS
                )
        );
    }

    //delete food
    @DeleteMapping(APIConstant.FOOD_ID)
    public ResponseEntity<MessageResponse> deleteFood(
            @PathVariable("food_id") UUID foodId
    ) {
        foodService.deleteFood(foodId);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.DELETE_FOOD_SUCCESS)
        );
    }

    //update food
    @PutMapping(APIConstant.FOOD_ID)
    @CachePut(value = "food", key = "#foodId")
    public ResponseEntity<MessageResponse> updateFood(
            @PathVariable("food_id") UUID foodId,
            @RequestBody FoodCreateRequest request
    ) {
        foodService.updateFood(foodId, request);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.UPDATE_FOOD_SUCCESS)
        );
    }

    //search food
    @GetMapping(APIConstant.SEARCH)
    @Cacheable(value = "food", key = "#req")
    public ResponseEntity<Page<FoodResponse>> searchFood(FoodSearchRequest req, PageInfo pageInfo) {
        var rs = foodService.search(req, pageInfo);
        return ResponseEntity.status(OK).body(rs);
    }

    @GetMapping(APIConstant.FOOD_ID)
    @Cacheable(value = "food", key = "#foodId")
    public ResponseEntity<Food> getFoodById(
            @PathVariable("food_id") UUID foodId
    ) {
        var rs = foodService.getDetailFood(foodId);
        return ResponseEntity.status(OK).body(rs);
    }
}
