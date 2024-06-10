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
import com.ooadprojectserver.restaurantmanagement.service.food.builder.ConcreteFoodBuilder;
import com.ooadprojectserver.restaurantmanagement.service.food.builder.FoodBuilder;
import com.ooadprojectserver.restaurantmanagement.service.food.builder.FoodDirector;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "food", allEntries = true)
    public void createFood(FoodCreateRequest request) {
        Optional<Food> optionalFood = foodRepository.findByName(request.getName());
        if (optionalFood.isPresent()) {
            throw new CustomException(APIStatus.FOOD_ALREADY_EXISTED);
        }

        FoodBuilder foodBuilder = new ConcreteFoodBuilder();
        FoodDirector foodDirector = new FoodDirector(foodBuilder, categoryRepository);
        foodDirector.construct(request);
        Food newFood = foodDirector.getFood();

        newFood.setCommonCreate(userDetailService.getIdLogin());
        foodRepository.save(newFood);
    }

    //delete food
    @Override
    @CacheEvict(value = "food", allEntries = true)
    public void deleteFood(UUID foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        foodRepository.delete(food);
    }

    @Override
    @CacheEvict(value = "food", allEntries = true)
    public void updateFood(UUID foodId, FoodCreateRequest updatingFood) {

        Food existedFood = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        Category updateCategory = categoryRepository.findById(updatingFood.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );

        //TODO: refactor update food
        existedFood.setName(updatingFood.getName());
        existedFood.setCategory(updateCategory);
        existedFood.setDescription(updatingFood.getDescription());
        existedFood.setProductImage(updatingFood.getProductImage());
        existedFood.setPrice(updatingFood.getPrice());
        existedFood.setStatus(updatingFood.getStatus());
        existedFood.setCommonUpdate(userDetailService.getIdLogin());

        foodRepository.save(existedFood);
    }

    @Cacheable(value = "food", key = "#foodId")
    public Food getDetailFood(UUID foodId) {
        return foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
    }

    @Override
    @Cacheable(value = "food")
    public Page<FoodResponse> search(FoodSearchRequest searchRequest, PageInfo pageInfo) {
        try{
            Thread.sleep(3000);
        }catch (Exception ignored){

        }
      return foodRepository.search(searchRequest, pageInfo);
    }
}
