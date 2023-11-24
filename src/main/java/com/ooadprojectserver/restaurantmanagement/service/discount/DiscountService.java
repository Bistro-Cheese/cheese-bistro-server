package com.ooadprojectserver.restaurantmanagement.service.discount;

import com.ooadprojectserver.restaurantmanagement.dto.request.DiscountRequest;
import com.ooadprojectserver.restaurantmanagement.model.discount.Discount;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {
    void create(DiscountRequest request);

    void update(Integer discountId, DiscountRequest request);

    void delete(Integer discountId);

    List<Discount> getAll();

    Discount getById(Integer discountId);

    BigDecimal calculateDiscount(BigDecimal total, Discount discount);
}
