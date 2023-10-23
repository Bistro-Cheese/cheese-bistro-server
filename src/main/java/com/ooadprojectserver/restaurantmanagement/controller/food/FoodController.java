package com.ooadprojectserver.restaurantmanagement.controller.food;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.service.food.FoodService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final FoodService foodService;

    ///get all food
    @GetMapping()
    public ResponseEntity<APIResponse<List<Food>>> getAllFoods(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ALL_FOODS_SUCCESS,
                        foodService.getAllFoods(request)
                )
        );
    }

    //create food
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping()
    public ResponseEntity<MessageResponse> createFood(
            @RequestBody FoodRequest request
    ) {
        foodService.createFood(request);
        return ResponseEntity.status(CREATED).body(
                new MessageResponse(
                        MessageConstant.CREATE_FOOD_SUCCESS
                )
        );
    }

    //delete food
    @PreAuthorize("hasRole('OWNER')")
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
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping(APIConstant.FOOD_ID)
    public ResponseEntity<MessageResponse> updateFood(
            @PathVariable("food_id") UUID foodId,
            @RequestBody FoodRequest request
    ) {
        foodService.updateFood(foodId, request);
        return ResponseEntity.status(OK).body(
                new MessageResponse(MessageConstant.UPDATE_FOOD_SUCCESS)
        );
    }

    //search food
    @GetMapping(APIConstant.SEARCH)
    public ResponseEntity<APIResponse<PagingResponseModel>> searchFood(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "search_key", required = false) String searchKey,
            @RequestParam(name = "min_price", required = false) BigDecimal minPrice,
            @RequestParam(name = "max_price", required = false) BigDecimal maxPrice,
            @RequestParam(name = "sort_case", defaultValue = "1") Integer sortCase,
            @RequestParam(name = "is_asc_sort", required = false) boolean isAscSort,
            @RequestParam(name = "page_number", defaultValue = "1") Integer pageNumber,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize
    ) {
        return ResponseEntity.status(OK).body(
                new APIResponse<>(
                        MessageConstant.SEARCH_FOOD_SUCCESS,
                        foodService.searchFood(category, searchKey,
                                minPrice, maxPrice, sortCase, isAscSort, pageNumber, pageSize)
                )
        );
    }
}