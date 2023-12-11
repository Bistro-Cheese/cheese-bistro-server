package com.ooadprojectserver.restaurantmanagement.service.order;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.DetailOrderResponse;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderService {
    DetailOrderResponse getByTableId(Integer tableId);
    Order getById(UUID orderId);
    void create(OrderRequest request);
    void update(OrderRequest request);
    void delete(UUID orderId);
    BigDecimal calculateSubTotal(UUID orderId);
}
