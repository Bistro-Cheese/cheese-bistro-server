package com.ooadprojectserver.restaurantmanagement.service.order;

import com.ooadprojectserver.restaurantmanagement.model.order.Order;

import java.util.UUID;

public interface OrderService {
    Order getById(UUID orderId);
    void create(Integer tableId);
    void delete(UUID orderId);
}
