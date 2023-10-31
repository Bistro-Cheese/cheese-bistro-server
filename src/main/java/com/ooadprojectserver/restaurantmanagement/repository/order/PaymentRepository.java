package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
