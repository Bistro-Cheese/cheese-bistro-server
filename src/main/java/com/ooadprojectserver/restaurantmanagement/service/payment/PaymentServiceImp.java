package com.ooadprojectserver.restaurantmanagement.service.payment;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderStatus;
import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.PaymentType;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderLineRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.OrderTableRepository;
import com.ooadprojectserver.restaurantmanagement.repository.order.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService{
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final OrderTableRepository orderTableRepository;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment createBill(PaymentRequest request, UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new CustomException(APIStatus.ORDER_NOT_FOUND)
        );
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new CustomException(APIStatus.ORDER_COMPLETED);
        }
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        orderTableRepository.updateTableStatusById(TableStatus.EMPTY, order.getOrderTable().getId());
        long total = 0L;
        List<OrderLine> orderLineList = orderLineRepository.findByOrder_Id(orderId);
        for (OrderLine orderLine : orderLineList) {
            total += orderLine.getFood().getPrice() * orderLine.getQuantity();
        }
        return paymentRepository.save(
                Payment.builder()
                        .order(order)
                        .paymentType(PaymentType.covertIntToPaymentType(request.getPaymentType()))
                        .total(total)
                        .build()
        );
    }
}
