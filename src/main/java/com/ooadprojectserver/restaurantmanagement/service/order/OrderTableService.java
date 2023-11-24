package com.ooadprojectserver.restaurantmanagement.service.order;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderTableRequest;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;

import java.util.List;

public interface OrderTableService {
    void create(OrderTableRequest request);
    void update(Integer tableId, OrderTableRequest request);
    void delete(Integer tableId);
    List<OrderTable> getAll();
    OrderTable getById(Integer tableId);
    void updateStatus(Integer tableId, TableStatus status);
}
