package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.service.food.FoodService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderLineServiceImpl implements OrderLineService {
    private final OrderLineRepository repository;
    private final OrderRepository orderRepository;

    private final FoodService foodService;

    @Override
    public void create(UUID orderId,OrderLineRequest req) {
        OrderLine newOrderLine = new OrderLine();

        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
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

    @Override
    public BigDecimal calculateSubTotal(UUID orderLineId) {
        OrderLine orderLine = getById(orderLineId);
        return BigDecimal.valueOf(orderLine.getQuantity())
                .multiply(orderLine.getFood().getPrice());
    }

    private OrderLine getOrderLineById(UUID orderLineId) {
        return repository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );
    }
}
