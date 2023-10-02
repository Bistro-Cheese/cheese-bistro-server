package com.ooadprojectserver.restaurantmanagement.service.food;

import com.ooadprojectserver.restaurantmanagement.dto.request.FoodRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.util.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.ApplicationException;
import com.ooadprojectserver.restaurantmanagement.exception.FoodException;
import com.ooadprojectserver.restaurantmanagement.model.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.repository.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.specification.FoodSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public List<Food> findAllProducts() {
        return foodRepository.findAll();
    }

    //create food
    @Override
    public Food createProduct(FoodRequest request) throws FoodException {
        Optional<Category> category = categoryRepository.findByName(request.getCategory());
        if (category.isEmpty()){
            throw new FoodException("Not Found Food");
        }
        var food = Food.builder()
                .name(request.getName())
                .category(category.get())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .product_image(request.getProductImage())
                .createdDate(new Date())
                .lastModifiedDate(new Date())
                .status(request.getStatus().getValue())
                .build();
        return foodRepository.save(food);
    }

    //delete food
    @Override
    public void deleteProduct(UUID foodId) throws FoodException {
        foodRepository.deleteById(foodId);
    }

    @Override
    public void updateProduct(UUID foodId, FoodRequest updatingFood) throws FoodException {
        Optional<Food> existingFood = foodRepository.findById(foodId);
        if (existingFood.isEmpty()){
            throw new FoodException("Food Not Found");
        }
        Optional<Category> updateCategory = categoryRepository.findByName(updatingFood.getCategory());
        if(updateCategory.isEmpty()){
            throw new ApplicationException(APIStatus.ERR_BAD_REQUEST);
        }
        Date lastModifiedDate = new Date();
        foodRepository.updateFoodById(updatingFood.getName(), updateCategory.get(), updatingFood.getDescription(),
                updatingFood.getQuantity(), updatingFood.getProductImage(), updatingFood.getPrice(), lastModifiedDate,
                updatingFood.getStatus().getValue(), existingFood.get().getId());
    }

    @Override
    public Food findFoodById(UUID id) throws FoodException {
        Optional<Food> foundFood = foodRepository.findById(id);
        if(foundFood.isPresent()){
            return foundFood.get();
        }else {
            throw new FoodException("Not Found Food");
        }
    }

    @Override
    public Page<Food> doFilterSearchSortPagingFood(String category, String searchKey, BigDecimal minPrice,BigDecimal maxPrice,
                                                   Integer sortCase, Boolean isAscSort, Integer pageNumber,
                                                   Integer pageSize) {
        return foodRepository.findAll(new FoodSpecification(category, searchKey, minPrice, maxPrice, sortCase, isAscSort), PageRequest.of(pageNumber, pageSize));
    }

}
