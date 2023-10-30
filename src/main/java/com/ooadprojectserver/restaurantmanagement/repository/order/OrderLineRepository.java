package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID> {
    @Transactional
    @Modifying
    @Query("update OrderLine o set o.quantity = ?1 where o.id = ?2")
    void updateQuantityById(Integer quantity, UUID id);

    @Query("select o from OrderLine o where o.order.id = ?1")
    List<OrderLine> findByOrder_Id(UUID id);
}
