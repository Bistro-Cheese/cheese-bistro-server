package com.ooadprojectserver.restaurantmanagement.repository.order;

import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OrderTableRepository extends JpaRepository<OrderTable, Integer> {
    Optional<OrderTable> findByTableNumber(Integer tableNumber);

    @Transactional
    @Modifying
    @Query("update OrderTable o set o.tableStatus = ?1 where o.id = ?2")
    void updateTableStatusById(TableStatus tableStatus, Integer id);

}
