package com.ooadprojectserver.restaurantmanagement.service.order;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;

import java.math.BigDecimal;
import java.util.UUID;

public interface OrderLineService {
    void create(UUID orderId, OrderLineRequest req);
    void update(UUID orderLineId, OrderLineRequest req);
    void delete(UUID orderLineId);
    OrderLine getById(UUID orderLineId);
    BigDecimal calculateSubTotal(UUID orderLineId);
}
