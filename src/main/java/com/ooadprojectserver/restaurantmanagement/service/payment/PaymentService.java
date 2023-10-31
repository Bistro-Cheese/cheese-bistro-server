package com.ooadprojectserver.restaurantmanagement.service.payment;

import com.ooadprojectserver.restaurantmanagement.dto.request.order.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<Payment> getAllPayments();
    Payment createBill(PaymentRequest request, UUID orderId);
}
