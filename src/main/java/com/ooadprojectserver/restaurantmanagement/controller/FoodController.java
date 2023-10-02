package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.ListFoodFilterRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.model.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.ApplicationException;
import com.ooadprojectserver.restaurantmanagement.exception.FoodException;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.service.food.FoodServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.FOOD)
public class FoodController {

    private final FoodServiceImpl foodService;

    @GetMapping(APIConstant.ALL_FOOD)
    public ResponseEntity<APIResponse<List<Food>>> getAllFoods() {
        return ResponseEntity.status(OK).body(
                new APIResponse<List<Food>>(
                        MessageConstant.GET_ALL_FOODS_SUCCESS,
                        foodService.findAllProducts()
                )
        );
    }

    @PostMapping(APIConstant.CREATE_FOOD)
    public ResponseEntity<APIResponse<Food>> createFood(@RequestBody FoodRequest request) throws FoodException {
        return ResponseEntity.status(CREATED).body(
                new APIResponse<Food>(
                        MessageConstant.FOOD_CREATED_SUCCESS,
                        foodService.createProduct(request)
                )
        );
    }

    @DeleteMapping(APIConstant.DELETE_FOOD)
    public ResponseEntity<String> deleteFood(@PathVariable("food_id") UUID foodId) throws FoodException {
        foodService.deleteProduct(foodId);
        return ResponseEntity.status(OK).body(
                MessageConstant.FOOD_DELETED_SUCCESS
        );
    }

    @PostMapping(APIConstant.UPDATE_FOOD)
    public ResponseEntity<String> updateFood(@PathVariable("food_id") UUID foodId, @RequestBody FoodRequest request)
            throws FoodException {
        foodService.updateProduct(foodId, request);
        return ResponseEntity.status(OK).body(
                MessageConstant.FOOD_UPDATE_SUCCESS
        );
    }

    @GetMapping(APIConstant.FOOD_BY_ID)
    public ResponseEntity<APIResponse<Food>> findFoodById(@PathVariable("food_id") UUID foodId) throws FoodException {
        return ResponseEntity.status(OK).body(
                new APIResponse<Food>(
                        MessageConstant.FIND_FOOD_SUCCESS,
                        foodService.findFoodById(foodId)
                )
        );
    }

    @GetMapping(APIConstant.FOOD_FILTER_LIST)
    public ResponseEntity<APIResponse<PagingResponseModel>> getFoodFilterList(@RequestBody ListFoodFilterRequest request)  {
        try {
            Page<Food> listFoodsFilter = foodService.doFilterSearchSortPagingFood(request.getCategory(), request.getSearchKey(),
                    request.getMinPrice(), request.getMaxPrice(), request.getSortCase(), request.isAscSort(),
                    request.getPageNumber(), request.getPageSize());
            PagingResponseModel finalRes = new PagingResponseModel(listFoodsFilter.getContent(),
                    listFoodsFilter.getTotalElements(), listFoodsFilter.getTotalPages(), listFoodsFilter.getNumber());
            return ResponseEntity.status(OK).body(
                    new APIResponse<PagingResponseModel>(
                            MessageConstant.FOOD_FILTER_SUCCESS,
                            finalRes
                    )
            );
        } catch (Exception ex) {
            throw new ApplicationException(APIStatus.GET_LIST_PRODUCT_ERROR);
        }
    }
}
