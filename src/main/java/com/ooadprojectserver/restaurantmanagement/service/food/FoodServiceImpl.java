package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.SearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.PagingResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Category;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.FoodStatus;
import com.ooadprojectserver.restaurantmanagement.repository.food.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.CompositionRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.inventory.InventoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.specification.FoodSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class FoodServiceImpl implements FoodService {
    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final CompositionRepository compositionRepository;
    private final InventoryRepository inventoryRepository;
    //get all foods
    @Override
    public List<Food> getAllFoods() {
        List<Food> foodList = foodRepository.findAll();
        updateFoodStatus(foodList);
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
    public PagingResponse searchFood(SearchRequest searchRequest) {
        searchRequest.getPagingRequest().checkValidPaging();
        Page<Food> foodPage = foodRepository.findAll(
                new FoodSpecification(searchRequest.getParams()), searchRequest.getPagingRequest().toPageRequest());
        return new PagingResponse(
                foodPage.getContent(),
                foodPage.getTotalElements(),
                foodPage.getTotalPages(),
                foodPage.getNumber() + 1
        );
    }

    private void updateFoodStatus(List<Food> foodList) {
        for (Food food : foodList) {
            boolean isOutOfStock = false;
            List<Composition> compositionList = compositionRepository.findByFood(food.getId());
            if (compositionList.isEmpty()) {
                foodRepository.updateStatusAndLastModifiedDateById(
                        FoodStatus.DRAFT.getValue(),
                        new Date(),
                        food.getId()
                );
                food.setStatus(FoodStatus.DRAFT.getValue());
                continue;
            }
            for (Composition composition : compositionList) {
                Long ingredient_id = composition.getIngredient().getId();
                Double quantity = inventoryRepository.findByIngredient_Id(ingredient_id).getQuantity();
                if (composition.getPortion() > quantity) {
                    isOutOfStock = true;
                    break;
                }
            }
            if (isOutOfStock) {
                foodRepository.updateStatusAndLastModifiedDateById(
                        FoodStatus.OUT_OF_STOCK.getValue(),
                        new Date(),
                        food.getId()
                );
                food.setStatus(FoodStatus.OUT_OF_STOCK.getValue());
            } else {
                foodRepository.updateStatusAndLastModifiedDateById(
                        FoodStatus.AVAILABLE.getValue(),
                        new Date(),
                        food.getId()
                );
                food.setStatus(FoodStatus.AVAILABLE.getValue());
            }
        }
    }
}
