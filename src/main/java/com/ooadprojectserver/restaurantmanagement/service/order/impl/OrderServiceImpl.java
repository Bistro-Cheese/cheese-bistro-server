package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.DetailOrderResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.StaffResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderLineService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderTableService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserDetailService userDetailService;
    private final StaffService staffService;
    private final OrderTableService orderTableService;
    private final OrderLineService orderLineService;

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    @Override
    public DetailOrderResponse getByTableId(Integer tableId) {

        Order order = orderRepository.findByOrderTable_IdAndStatus(tableId, OrderStatus.PENDING).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );

        List<OrderLineResponse> orderLineList = orderLineRepository.search(OrderLineSearchRequest.builder()
                .orderId(order.getId())
                .build());

        StaffResponse staffResponse = StaffResponse.builder()
                .id(order.getStaff().getId())
                .firstName(order.getStaff().getFirstName())
                .lastName(order.getStaff().getLastName())
                .avatar(order.getStaff().getAvatar())
                .username(order.getStaff().getUsername())
                .build();

        return DetailOrderResponse.builder()
                .id(order.getId())
                .staff(staffResponse)
                .orderLines(orderLineList)
                .status(order.getStatus().toString())
                .build();
    }

    @Override
    public Order getById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
    }

    @Override
    public void create(OrderRequest request) {
        Staff staff = getStaff();

        OrderTable orderTable = orderTableService.getById(request.getTableId());

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
        UUID orderId = orderRepository.save(newOrder).getId();
        request.getOrderLines().forEach(orderLineRequest -> orderLineService.create(orderId, orderLineRequest));
    }

    @Override
    public void delete(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );

        // delete order_lines of order
        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);
        orderLineList.stream().map(OrderLine::getId).forEach(orderLineService::delete);

        // delete order
        orderRepository.delete(order);

        // update table status
        orderTableService.updateStatus(order.getOrderTable().getId(), TableStatus.EMPTY);
    }

    @Override
    public BigDecimal calculateSubTotal(UUID orderId) {
        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);

        BigDecimal subTotal = BigDecimal.ZERO;
        for (OrderLine orderLine : orderLineList) {
            subTotal = subTotal.add(orderLineService.calculateSubTotal(orderLine.getId()));
        }

        return subTotal;
    }

    private Staff getStaff() {
        UUID staffId = userDetailService.getIdLogin();
        return (Staff) staffService.getUserById(staffId);
    }
}
