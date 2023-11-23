package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderTableService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserDetailService userDetailService;
    private final StaffService staffService;
    private final OrderTableService orderTableService;

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    @Override
    public Order getById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
    }

    @Override
    public void create(Integer tableId) {
        Staff staff = getStaff();

        OrderTable orderTable = orderTableService.getById(tableId);

        if (!orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
            throw new CustomException(APIStatus.ORDER_TABLE_NOT_EMPTY);
        }

        orderTableService.updateStatus(orderTable.getId(), TableStatus.OCCUPIED);

        Order newOrder = Order.builder()
                .staff(staff)
                .orderTable(orderTable)
                .status(OrderStatus.PENDING)
                .build();

        newOrder.setCommonCreate(userDetailService.getIdLogin());
        orderRepository.save(newOrder);
    }

    @Override
    public void delete(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );

        // delete order_lines of order
        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);
        orderLineList.stream().map(OrderLine::getId).forEach(orderLineRepository::deleteById);

        // delete order
        orderRepository.delete(order);

        // update table status
        orderTableService.updateStatus(order.getOrderTable().getId(), TableStatus.EMPTY);
    }

    private Staff getStaff() {
        UUID staffId = userDetailService.getIdLogin();
        return (Staff) staffService.getUserById(staffId);
    }
}
