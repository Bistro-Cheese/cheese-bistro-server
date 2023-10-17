package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.repository.food.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.specification.FoodSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;

    //get all foods
    @Override
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    //create food
    @Override
    public void createFood(FoodRequest request) {
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );
        foodRepository.save(
                Food.builder()
                        .name(request.getName())
                        .category(category)
                        .description(request.getDescription())
                        .price(request.getPrice())
                        .productImage(request.getProductImage())
                        .createdDate(new Date())
                        .lastModifiedDate(new Date())
                        .status(request.getStatus())
                        .build()
        );
    }

    //delete food
    @Override
    public void deleteFood(UUID foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        foodRepository.delete(food);
    }

    @Override
    public void updateFood(UUID foodId, FoodRequest updatingFood) {
        Food existingFood = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        Category updateCategory = categoryRepository.findById(updatingFood.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );
        Date lastModifiedDate = new Date();
        foodRepository.updateFoodById(
                updatingFood.getName(),
                updateCategory,
                updatingFood.getDescription(),
                updatingFood.getProductImage(),
                updatingFood.getPrice(),
                lastModifiedDate,
                updatingFood.getStatus(),
                existingFood.getId()
        );
    }

    @Override
    public PagingResponseModel doFilterSearchSortPagingFood(
            String category, String searchKey, BigDecimal minPrice,
            BigDecimal maxPrice, Integer sortCase, Boolean isAscSort,
            Integer pageNumber, Integer pageSize
    ) {
        if (pageSize < 1 || pageNumber < 1) {
            throw new CustomException(APIStatus.ERR_PAGINATION);
        }
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Food> foodPage = foodRepository.findAll(
                new FoodSpecification(
                        category,
                        searchKey,
                        minPrice,
                        maxPrice,
                        sortCase,
                        isAscSort
                ), pageable);
        return new PagingResponseModel(
                foodPage.getContent(),
                foodPage.getTotalElements(),
                foodPage.getTotalPages(),
                foodPage.getNumber() + 1
        );
    }

}
