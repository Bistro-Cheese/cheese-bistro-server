package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodCreateRequest;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.food.FoodSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.food.FoodResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.repository.food.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService {

    private final UserDetailService userDetailService;

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    //get all foods
    @Override
    public List<Food> getAllFoods() {
        List<Food> foodList = foodRepository.findAll();
        Comparator<Food> foodComparator = Comparator.comparing(
                food -> food.getCategory().getId()
        );
        foodList.sort(foodComparator);
        return foodList;
    }

    //create food
    @Override
    public void createFood(FoodCreateRequest request) {
        //TODO: refactor create food
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );
        Optional<Food> optionalFood = foodRepository.findByName(request.getName());
        if (optionalFood.isPresent()) {
            throw new CustomException(APIStatus.FOOD_ALREADY_EXISTED);
        } else {
            Food newFood = copyProperties(request, Food.class);
            newFood.setCategory(category);
            newFood.setCommonCreate(userDetailService.getIdLogin());
            foodRepository.save(newFood);
        }
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
    public void updateFood(UUID foodId, FoodCreateRequest updatingFood) {
        Food existingFood = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        Category updateCategory = categoryRepository.findById(updatingFood.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );

        //TODO: refactor update food
        updatingFood = copyProperties(updatingFood, FoodCreateRequest.class);
        foodRepository.save(existingFood);
    }

    public Food getDetailFood(UUID foodId) {
        return foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
    }

    @Override
    public Page<FoodResponse> search(FoodSearchRequest searchRequest, PageInfo pageInfo) {
      return foodRepository.search(searchRequest, pageInfo);
    }
}
