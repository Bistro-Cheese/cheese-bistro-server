package com.ooadprojectserver.restaurantmanagement.repository.payment;

import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
