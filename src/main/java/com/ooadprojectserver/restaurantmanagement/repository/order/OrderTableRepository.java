package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
    Optional<OrderTable> findByTableNumber(Integer tableNumber);
}
