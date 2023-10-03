package com.ooadprojectserver.restaurantmanagement.boostrap;

import com.github.javafaker.Faker;
import com.ooadprojectserver.restaurantmanagement.model.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.repository.CategoryRepository;
import com.ooadprojectserver.restaurantmanagement.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class SampleDataLoader implements CommandLineRunner {
    Logger logger = LoggerFactory.getLogger(SampleDataLoader.class);

    private final Faker faker;

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;



    @Override
    public void run(String... args) throws Exception {
        logger.warn("Loading Sample Data");
        //create 100 rows of Food
        CreateListFakeFood();
    }

    private Category randomCategory() {
        List<String> listCategory = new ArrayList<String>(
                List.of(
                        "appetizer",
                        "dessert",
                        "drink",
                        "main Course"
                )
        );
        Random categoryName = new Random();

        String randomCategoryName = listCategory.get(categoryName.nextInt(listCategory.size()));
        Category randomCategory = categoryRepository.findByName(randomCategoryName).get();
        return randomCategory;
    }

    private void CreateListFakeFood(){
        List<Food> listFoods = IntStream.rangeClosed(1,100)
                .mapToObj(i ->
                        new Food(
                                UUID.randomUUID(),
                                faker.name().name(),
                                randomCategory(),
                                faker.lorem().characters(100),
                                faker.number().numberBetween(100,400),
                                faker.name().name(),
                                BigDecimal.valueOf(faker.number().numberBetween(50000, 1000000)),
                                faker.number().numberBetween(1,3),
                                new  Date(),
                                new Date())
                ).toList();

        foodRepository.saveAll(listFoods);
    }

}
