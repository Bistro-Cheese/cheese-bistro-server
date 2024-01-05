package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderStatus;
import com.ooadprojectserver.restaurantmanagement.repository.custom.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {
    Optional<Order> findByOrderTable_IdAndStatus(Integer id, OrderStatus status);
    List<Order> findAllByStatus(OrderStatus status);
}
