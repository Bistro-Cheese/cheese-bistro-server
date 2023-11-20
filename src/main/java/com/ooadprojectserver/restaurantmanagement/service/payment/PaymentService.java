package com.ooadprojectserver.restaurantmanagement.service.payment;

import com.ooadprojectserver.restaurantmanagement.dto.request.TransferMethodRequest;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    List<Payment> getAll();

    Payment getById(UUID id);

    void create(TransferMethodRequest req);

    void update(UUID id, TransferMethodRequest req);

    void delete(UUID id);
}
