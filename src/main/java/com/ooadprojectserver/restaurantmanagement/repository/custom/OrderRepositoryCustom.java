package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderSearchResponse;

import java.util.List;

public interface OrderRepositoryCustom {
    List<OrderSearchResponse> search(OrderSearchRequest request);
}
