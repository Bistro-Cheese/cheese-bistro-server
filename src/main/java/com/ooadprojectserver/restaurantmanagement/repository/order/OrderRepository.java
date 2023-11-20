package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.Order;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("select o from Order o where o.staff.id = ?1 and o.orderTable.id = ?2 and o.status = ?3")
    Order findOrder(UUID id, Integer table_id, OrderStatus status);

}
