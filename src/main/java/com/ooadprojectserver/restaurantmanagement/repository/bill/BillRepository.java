package com.ooadprojectserver.restaurantmanagement.repository.bill;

import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;
import com.ooadprojectserver.restaurantmanagement.repository.custom.BillRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID>, BillRepositoryCustom {
    Bill findByOrder_Id(UUID orderId);
}
