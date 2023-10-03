package com.ooadprojectserver.restaurantmanagement.repository;

import com.ooadprojectserver.restaurantmanagement.model.Category;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface FoodRepository extends JpaRepository<Food, UUID>, JpaSpecificationExecutor<Food> {


    @Transactional
    @Modifying
    @Query("""
            update Food f set f.name = ?1, f.category = ?2, f.description = ?3, f.quantity = ?4, f.product_image = ?5, f.price = ?6, f.lastModifiedDate = ?7, f.status = ?8
            where f.id = ?9""")
    void updateFoodById(String name, Category category, String description,
                       Integer quantity, String product_image,
                       BigDecimal price, Date lastModifiedDate,
                       Integer status, UUID id);

}
