package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponseModel;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.user.type.User;
import com.ooadprojectserver.restaurantmanagement.repository.food.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.CompositionRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.specification.FoodSpecification;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.authentication.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService {
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final CompositionRepository compositionRepository;
    //get all foods
    @Override
    public List<Food> getAllFoods(
            HttpServletRequest request
    ) {
        String username = jwtService.getUsernameFromHeader(request);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        List<Food> foodList = new ArrayList<>();
        switch (user.getRole()) {
            case 1 -> foodList.addAll(foodRepository.findByStatus(1));
            case 2 -> {
                foodList.addAll(foodRepository.findByStatus(1));
                foodList.addAll(foodRepository.findByStatus(2));
            }
            case 3 -> foodList.addAll(foodRepository.findAll());
        };
        Comparator<Food> foodComparator = Comparator.comparing(
                food -> food.getCategory().getId()
        );
        foodList.sort(foodComparator);
        return foodList;
    }

    //create food
    @Override
    public void createFood(FoodRequest request) {
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(
                () -> new CustomException(APIStatus.CATEGORY_NOT_FOUND)
        );
        Optional<Food> optionalFood = foodRepository.findByName(request.getName());
        if (optionalFood.isPresent()) {
            throw new CustomException(APIStatus.FOOD_ALREADY_EXISTED);
        } else {
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
    }

    //delete food
    @Override
    public void deleteFood(UUID foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
        compositionRepository.deleteByFood(food);
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

    public Food getDetailFood(UUID foodId) {
        return foodRepository.findById(foodId).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );
    }

    @Override
    public PagingResponseModel searchFood(
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
