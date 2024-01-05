package com.ooadprojectserver.restaurantmanagement.repository.bill;

import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    Bill findByOrder_Id(UUID orderId);
}
