package com.ooadprojectserver.restaurantmanagement.repository.bill;

import com.ooadprojectserver.restaurantmanagement.model.discount.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
}
