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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public List<OrderResponse> getOrders() {
        var staff = getStaff();
        List<OrderResponse> orderResponseList = new ArrayList<>();

        List<OrderTable> orderTableList = orderTableRepository.findAll();

        //method get all orders
        getAllOrders(orderResponseList, orderTableList, staff);
        return orderResponseList;
    }

    @Override
    public void createOrder(OrderRequest orderRequest) {
        Staff staff = getStaff();

        if (staff != null)
            logger.info("staff order: " + staff.getFirstName());

        OrderTable orderTable = orderTableRepository.findById(orderRequest.getTableId()).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_TABLE_NOT_FOUND)
        );
        if (!orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
            throw new CustomException(APIStatus.ORDER_TABLE_NOT_EMPTY);
        }
        orderTableRepository.updateTableStatusById(TableStatus.OCCUPIED, orderTable.getId());
        Order newOrder = Order.builder()
                .staff(staff)
                .orderTable(orderTable)
                .orderDate(new Date())
                .status(OrderStatus.PENDING)
                .build();
        logger.info("order: " + newOrder.getStatus());
        orderRepository.save(newOrder);
    }

    @Override
    public void deleteOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );

        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);

        //add food quantity back to inventory
        returnBackToInventoryWhenDelete(orderLineList);

        orderLineList.stream().map(OrderLine::getId).forEach(orderLineRepository::deleteById);
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

        var orderLine = OrderLine.builder()
                .order(order)
                .food(food)
                .quantity(orderLineRequest.getQuantity())
                .build();

        List<Composition> compositionList = compositionRepository.findByFood(food.getId());
        compositionList.forEach(composition -> {
            inventoryService.getIngredientFromInventory(composition, orderLine);
        });

        orderLineRepository.save(orderLine);
    }

    @Override
    public void updateOrderLine(UUID orderLineId, OrderLineRequest orderLineRequest) {
        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );

        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        if (orderLineRequest.getQuantity() < orderLine.getQuantity()) {
            compositionList.forEach(composition -> {
                composition.setPortion(composition.getPortion() * (orderLine.getQuantity() - orderLineRequest.getQuantity()));
                inventoryService.returnIngredientToInventory(composition, orderLine);
            });
        } else if (orderLineRequest.getQuantity() > orderLine.getQuantity()) {
            compositionList.forEach(composition -> {
                composition.setPortion(composition.getPortion() * (orderLineRequest.getQuantity() - orderLine.getQuantity()));
                inventoryService.getIngredientFromInventory(composition, orderLine);
            });
        }

        orderLineRepository.updateQuantityById(orderLineRequest.getQuantity(), orderLineId);
    }

    @Override
    public void deleteOrderLine(UUID orderLineId) {
        OrderLine orderLine = orderLineRepository.findById(orderLineId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_LINE_NOT_FOUND)
        );
        orderLineRepository.deleteById(orderLineId);
    }

    //private methods
    private void getAllOrders( List<OrderResponse> orderResponseList, List<OrderTable> orderTableList, Staff staff) {
        for (OrderTable orderTable : orderTableList) {
            if (orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
                var orderResponse = OrderResponse.builder()
                        .table(tableInfoResponseBuild(orderTable))
                        .build();
                orderResponseList.add(orderResponse);
                continue;
            }
            List<OrderLineResponse> orderLineResponseList = new ArrayList<>();
            Order order = orderRepository.findOrder(staff.getId(), orderTable.getId(), OrderStatus.PENDING);
            List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(order.getId());
            orderLineList.stream().map(this::orderLineResponseBuild).forEach(orderLineResponseList::add);
            var orderResponse =  OrderResponse.builder()
                    .table(tableInfoResponseBuild(orderTable))
                    .orderId(order.getId())
                    .orderLines(orderLineResponseList)
                    .build();
            orderResponseList.add(orderResponse);
        }
    }



    private void returnBackToInventoryWhenDelete(List<OrderLine> orderLineList){
        orderLineList.forEach(this::handleCompositionWhenDelete);
    }

    private void handleCompositionWhenDelete(OrderLine orderLine){
        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        compositionList.forEach(composition -> {
            inventoryService.returnIngredientToInventory(composition, orderLine);
        });
        orderLineRepository.deleteById(orderLine.getId());
    }

    private void handleCompositionWhenUsing(OrderLine orderLine){
        List<Composition> compositionList = compositionRepository.findByFood(orderLine.getFood().getId());
        compositionList.forEach(composition -> {
            inventoryService.getIngredientFromInventory(composition, orderLine);
        });
        orderLineRepository.deleteById(orderLine.getId());
    }

    private Staff getStaff() {
        UUID staffId = userDetailService.getIdLogin();
        return (Staff) userRepository.findById(staffId).orElseThrow(
                () -> new CustomException(APIStatus.USER_NOT_FOUND)
        );
    }

    private TableInfoResponse tableInfoResponseBuild (OrderTable orderTable) {
        return TableInfoResponse.builder()
                .id(orderTable.getId())
                .number(orderTable.getTableNumber())
                .status(orderTable.getTableStatus().ordinal())
                .build();
    }

    private OrderLineResponse orderLineResponseBuild(OrderLine orderLine) {
        return OrderLineResponse.builder()
                .id(orderLine.getId())
                .foodId(orderLine.getFood().getId())
                .foodName(orderLine.getFood().getName())
                .foodPrice(orderLine.getFood().getPrice())
                .quantity(orderLine.getQuantity())
                .build();
    }
}
