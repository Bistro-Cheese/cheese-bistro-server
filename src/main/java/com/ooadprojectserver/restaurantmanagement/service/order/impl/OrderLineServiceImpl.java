package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.service.food.FoodService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderLineService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {
    private final OrderLineRepository repository;

    private final FoodService foodService;
    private final OrderService orderService;

    @Override
    public void create(OrderLineRequest req) {
        OrderLine newOrderLine = new OrderLine();

        Order order = orderService.getById(req.getOrderId());
        Food food = foodService.getDetailFood(req.getFoodId());

        newOrderLine.setOrder(order);
        newOrderLine.setFood(food);
        newOrderLine.setQuantity(req.getQuantity());

        repository.save(newOrderLine);
    }

    @Override
    public void update(UUID orderLineId, OrderLineRequest req) {
        OrderLine orderLine = getOrderLineById(orderLineId);
        orderLine.setQuantity(req.getQuantity());
        repository.save(orderLine);
    }

    @Override
    public void delete(UUID orderLineId) {
        OrderLine orderLine = getOrderLineById(orderLineId);
        repository.delete(orderLine);
    }

    @Override
    public OrderLine getById(UUID orderLineId) {
        return getOrderLineById(orderLineId);
    }

    private OrderLine getOrderLineById(UUID orderLineId) {
        return repository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );
    }
}
