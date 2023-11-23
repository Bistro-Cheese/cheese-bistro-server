package com.ooadprojectserver.restaurantmanagement.service.order;

import com.ooadprojectserver.restaurantmanagement.dto.request.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;

import java.util.List;
import java.util.UUID;

public interface OrderLineService {
    void create(OrderLineRequest req);
    void update(UUID orderLineId, OrderLineRequest req);
    void delete(UUID orderLineId);
    OrderLine getById(UUID id);
}
