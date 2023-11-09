package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.TableInfoResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.food.CompositionRepository;
import com.ooadprojectserver.restaurantmanagement.repository.food.FoodRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.repository.user.UserRepository;
import com.ooadprojectserver.restaurantmanagement.service.inventory.InventoryService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserDetailService userDetailService;
    private final InventoryService inventoryService;

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineRepository orderLineRepository;
    private final CompositionRepository compositionRepository;

    @Override
    public List<OrderResponse> getOrders() {
        UUID staffId = userDetailService.getIdLogin();
        Staff staff = (Staff) userRepository.findById(staffId).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );

        List<OrderResponse> orderResponseList = new ArrayList<>();

        List<OrderTable> orderTableList = orderTableRepository.findAll();
        for (OrderTable orderTable : orderTableList) {
            if (orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
                orderResponseList.add(
                        OrderResponse.builder()
                                .table(
                                        TableInfoResponse.builder()
                                                .id(orderTable.getId())
                                                .number(orderTable.getTableNumber())
                                                .status(orderTable.getTableStatus().ordinal())
                                                .build()
                                )
                                .build()
                );
                continue;
            }
            List<OrderLineResponse> orderLineResponseList = new ArrayList<>();
            Order order = orderRepository.findOrder(staff.getId(), orderTable.getId(), OrderStatus.PENDING);
            List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(order.getId());
            for (OrderLine orderLine : orderLineList) {
                orderLineResponseList.add(
                        OrderLineResponse.builder()
                                .id(orderLine.getId())
                                .foodId(orderLine.getFood().getId())
                                .foodName(orderLine.getFood().getName())
                                .foodPrice(orderLine.getFood().getPrice())
                                .quantity(orderLine.getQuantity())
                                .build()
                );
            }
            orderResponseList.add(
                    OrderResponse.builder()
                            .table(
                                    TableInfoResponse.builder()
                                            .id(orderTable.getId())
                                            .number(orderTable.getTableNumber())
                                            .status(orderTable.getTableStatus().ordinal())
                                            .build()
                            )
                            .orderId(order.getId())
                            .orderLines(orderLineResponseList)
                            .build()
            );
        }
        return orderResponseList;
    }

    @Override
    public void createOrder(OrderRequest orderRequest) {
        UUID staffId = userDetailService.getIdLogin();
        Staff staff = (Staff) userRepository.findById(staffId).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getTableId()).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_TABLE_NOT_FOUND)
        );
        if (!orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
            throw new CustomException(APIStatus.ORDER_TABLE_NOT_EMPTY);
        }
        orderTableRepository.updateTableStatusById(TableStatus.OCCUPIED, orderTable.getId());
        orderRepository.save(Order.builder()
                .staff(staff)
                .orderTable(orderTable)
                .orderDate(new Date())
                .status(OrderStatus.PENDING)
                .build());
    }

    @Override
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);
        for (OrderLine orderLine : orderLineList) {
            List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
            for (Composition composition : compositionList) {
                inventoryService.importIngredient(
                        (double) (composition.getPortion() * orderLine.getQuantity()),
                        composition.getIngredient().getId()
                );
            }
            orderLineRepository.deleteById(orderLine.getId());
        }
        orderRepository.deleteById(orderId);
        orderTableRepository.updateTableStatusById(TableStatus.EMPTY, order.getOrderTable().getId());
    }

    @Override
    public void createOrderLine(UUID orderId, OrderLineRequest orderLineRequest) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
        Food food = foodRepository.findById(orderLineRequest.getFoodId()).orElseThrow(
                () -> new CustomException(APIStatus.FOOD_NOT_FOUND)
        );

        List<Composition> compositionList = compositionRepository.findByFood(food.getId());
        for (Composition composition : compositionList) {
            inventoryService.useIngredient(
                    composition.getIngredient().getId(),
                    composition.getPortion() * orderLineRequest.getQuantity()
            );
        }

        orderLineRepository.save(OrderLine.builder()
                .order(order)
                .food(food)
                .quantity(orderLineRequest.getQuantity())
                .build());
    }

    @Override
    public void updateOrderLine(UUID orderLineId, OrderLineRequest orderLineRequest) {
        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );

        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        if (orderLineRequest.getQuantity() < orderLine.getQuantity()) {
            for (Composition composition : compositionList) {
                inventoryService.importIngredient(
                        (double) (composition.getPortion() * (orderLine.getQuantity() - orderLineRequest.getQuantity())),
                        composition.getIngredient().getId()
                );
            }
        } else {
            for (Composition composition : compositionList) {
                inventoryService.useIngredient(
                        composition.getIngredient().getId(),
                        composition.getPortion() * (orderLineRequest.getQuantity() - orderLine.getQuantity())
                );
            }
        }
        orderLineRepository.updateQuantityById(
                orderLineRequest.getQuantity(),
                orderLineId
        );
    }

    @Override
    public void deleteOrderLine(UUID orderLineId) {
        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );
        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        for (Composition composition : compositionList) {
            inventoryService.importIngredient(
                    (double) (composition.getPortion() * orderLine.getQuantity()),
                    composition.getIngredient().getId()
            );
        }
        orderLineRepository.deleteById(orderLineId);
    }
}
