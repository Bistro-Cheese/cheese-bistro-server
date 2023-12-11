package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.OrderLine;
import com.ooadprojectserver.restaurantmanagement.repository.custom.OrderLineRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID>, OrderLineRepositoryCustom {
    Optional<OrderLine> findByOrder_IdAndFood_Id(UUID id, UUID foodId);
    List<OrderLine> findByOrder_Id(UUID id);
}
