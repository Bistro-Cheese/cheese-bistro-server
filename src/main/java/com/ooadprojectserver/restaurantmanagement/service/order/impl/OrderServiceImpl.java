package com.ooadprojectserver.restaurantmanagement.service.order.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.customer.CustomerCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderSearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.DetailOrderResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderSearchResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.StaffResponse;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.customer.Customer;
import com.ooadprojectserver.restaurantmanagement.model.discount.Discount;
import com.ooadprojectserver.restaurantmanagement.model.order.*;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.service.customer.CustomerService;
import com.ooadprojectserver.restaurantmanagement.service.discount.DiscountService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderLineService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderTableService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserDetailService userDetailService;
    private final StaffService staffService;
    private final OrderTableService orderTableService;
    private final OrderLineService orderLineService;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final DiscountService discountService;
    private final CustomerService customerService;

    @Override
    public DetailOrderResponse getByTableId(Integer tableId) {

        Order order = getOrderByTableIdAndStatus(tableId);

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
                .subTotal(order.getSubTotal())
                .total(order.getTotal())
                .status(order.getStatus())
                .build();
    }

    @Override
    public Order getById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public void create(OrderRequest request) {
        Staff staff = getStaff();

        OrderTable orderTable = orderTableService.getById(request.getTableId());

        // Check table status
        if (!orderTable.getTableStatus().equals(TableStatus.EMPTY)) {
            throw new CustomException(APIStatus.ORDER_TABLE_NOT_EMPTY);
        }

        // Update table status
        if (request.getStatus().equals( OrderStatus.PENDING)) {
            orderTableService.updateStatus(orderTable.getId(), TableStatus.OCCUPIED);
        }


        Order newOrder = Order.builder()
                .staff(staff)
                .numberOfCustomer(request.getNumberOfCustomer())
                .orderTable(orderTable)
                .status(request.getStatus())
                .build();
        newOrder.setCommonCreate(userDetailService.getIdLogin());

        UUID orderId = orderRepository.save(newOrder).getId();

        request.getOrderLines().forEach(orderLineRequest -> orderLineService.create(orderId, orderLineRequest));

        // calculate price of order when not use discount and sale for customer
        newOrder.setSubTotal(calculateSubTotal(newOrder.getId()));

        //Calculate price after apply discount
        if (request.getDiscountId() != null) {
            applyDiscount(newOrder, request.getDiscountId());
        } else {
            newOrder.setTotal(newOrder.getSubTotal());
        }

        // Calculate deposit
        newOrder.calculateDeposit();

        // Create customer
        BigDecimal mustPay = newOrder.getTotal().subtract(newOrder.getDeposit());
        CustomerCreateRequest customerCreateRequest = copyProperties(request, CustomerCreateRequest.class);
        customerCreateRequest.setSpend(mustPay);
        Customer customer = customerService.create(customerCreateRequest);

        newOrder.setCustomer(customer);
        newOrder.setTotal(customer.calculateDiscount(newOrder.getTotal()));
        newOrder.setCusInTime(request.getCusIn());

        orderRepository.save(newOrder);
    }

    @Override
    public void update(OrderRequest request) {

        Order updatingOrder = getOrderByTableIdAndStatus(request.getTableId());

        List<OrderLineRequest> orderLineRequestList = request.getOrderLines();

        if (request.getOrderLines() != null && !request.getOrderLines().isEmpty()) {
            for (OrderLineRequest orderLineRequest : orderLineRequestList) {
                orderLineRepository.findByOrder_IdAndFood_Id(updatingOrder.getId(),
                        orderLineRequest.getFoodId()).ifPresentOrElse(
                        orderLine1 -> {
                            orderLineService.update(orderLine1.getId(), orderLineRequest);
                        },
                        () -> {
                            orderLineService.create(updatingOrder.getId(), orderLineRequest);
                        }
                );
            }
            updatingOrder.setSubTotal(calculateSubTotal(updatingOrder.getId()));
        }


        if (request.getDiscountId() != null) {
            applyDiscount(updatingOrder, request.getDiscountId());
        } else {
            updatingOrder.setTotal(updatingOrder.getSubTotal());
        }

        orderRepository.save(updatingOrder);
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
    public List<OrderSearchResponse> search(OrderSearchRequest status) {
        return orderRepository.search(status);
    }

    private void applyDiscount(Order order, Integer discountId) {
        Discount discount = discountService.getById(discountId);
        if (!discount.canUseDiscount()){
            throw new CustomException(APIStatus.DISCOUNT_CANNOT_USE);
        }
        order.setDiscount(discount);
        BigDecimal total = order.getSubTotal().subtract(discount.calculateDiscount(order.getSubTotal()));
        order.setTotal(total);
    }

    private BigDecimal calculateSubTotal(UUID orderId) {
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

    private Order getOrderByTableIdAndStatus(Integer tableId) {
        return orderRepository.findByOrderTable_IdAndStatus(tableId, OrderStatus.PENDING).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
    }
}
