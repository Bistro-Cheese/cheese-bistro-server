package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;

import java.util.List;

public interface OrderLineRepositoryCustom {
    List<OrderLineResponse> search(OrderLineSearchRequest request);
}
