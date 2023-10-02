package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.FOOD)
public class FoodController {

    private final FoodServiceImpl foodService;

    ///get all food
    @GetMapping()
    public ResponseEntity<APIResponse<List<Food>>> getAllFoods() {
        return ResponseEntity.status(OK).body(
                new APIResponse<List<Food>>(
                        MessageConstant.GET_ALL_FOODS_SUCCESS,
                        foodService.findAllProducts()
                )
        );
    }

    //create food
    @PostMapping()
    public ResponseEntity<APIResponse<Food>> createFood(@RequestBody FoodRequest request) throws FoodException {
        return ResponseEntity.status(CREATED).body(
                new APIResponse<Food>(
                        MessageConstant.FOOD_CREATED_SUCCESS,
                        foodService.createProduct(request)
                )
        );
    }

    //delete food
    @DeleteMapping(APIConstant.FOOD_ID)
    public ResponseEntity<String> deleteFood(@PathVariable("food_id") UUID foodId) throws FoodException {
        foodService.deleteProduct(foodId);
        return ResponseEntity.status(OK).body(
                MessageConstant.FOOD_DELETED_SUCCESS
        );
    }

    //update food
    @PutMapping(APIConstant.FOOD_ID)
    public ResponseEntity<String> updateFood(@PathVariable("food_id") UUID foodId, @RequestBody FoodRequest request)
            throws FoodException {
        foodService.updateProduct(foodId, request);
        return ResponseEntity.status(OK).body(
                MessageConstant.FOOD_UPDATE_SUCCESS
        );
    }

    //find detail food (by id)
    @GetMapping(APIConstant.FOOD_ID)
    public ResponseEntity<APIResponse<Food>> findFoodById(@PathVariable("food_id") UUID foodId) throws FoodException {
        return ResponseEntity.status(OK).body(
                new APIResponse<Food>(
                        MessageConstant.FIND_FOOD_SUCCESS,
                        foodService.findFoodById(foodId)
                )
        );
    }

    //search food
    @GetMapping(value = APIConstant.SEARCH_FOOD)
    public ResponseEntity<APIResponse<PagingResponseModel>> getFoodFilterList(@RequestParam(name = "category", required = false) String category,
                                                                              @RequestParam(name = "search_key", required = false) String searchKey,
                                                                              @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
                                                                              @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
                                                                              @RequestParam(name="sort_case", defaultValue = "1") Integer sortCase,
                                                                              @RequestParam(name="is_asc_sort", required = false)  boolean isAscSort,
                                                                              @RequestParam(name="page_number", defaultValue = "0") Integer pageNumber,
                                                                              @RequestParam(name="page_size", defaultValue = "10") Integer pageSize)  {
        try {
            Page<Food> listFoodsFilter = foodService.doFilterSearchSortPagingFood(category, searchKey,
                    minPrice, maxPrice, sortCase, isAscSort, pageNumber, pageSize);
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
