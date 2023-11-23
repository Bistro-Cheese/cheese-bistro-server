package com.ooadprojectserver.restaurantmanagement.service.payment;

import com.ooadprojectserver.restaurantmanagement.dto.request.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<Payment> getAll();

    Payment getById(UUID id);

    void create(PaymentRequest req);

    void update(UUID id, PaymentRequest req);

    void delete(UUID id);
}
