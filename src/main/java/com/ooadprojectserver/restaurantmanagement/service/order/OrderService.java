package com.ooadprojectserver.restaurantmanagement.service.order;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    List<OrderResponse> getOrders(HttpServletRequest request);
    void createOrder(HttpServletRequest request, OrderRequest orderRequest);
    void deleteOrder(UUID orderId);
    void createOrderLine(UUID orderId, OrderLineRequest orderLineRequest);
    void updateOrderLine(UUID orderLineId, OrderLineRequest orderLineRequest);
    void deleteOrderLine(UUID orderLineId);
}
