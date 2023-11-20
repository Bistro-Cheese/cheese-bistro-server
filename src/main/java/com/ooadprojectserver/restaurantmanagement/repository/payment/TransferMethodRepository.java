package com.ooadprojectserver.restaurantmanagement.repository.payment;

import com.ooadprojectserver.restaurantmanagement.model.payment.TransferMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransferMethodRepository extends JpaRepository<TransferMethod, Integer> {
    Optional<TransferMethod> findByAccountNumber(String accountNumber);

    List<TransferMethod> findByIsActive(Boolean isActive);
}
