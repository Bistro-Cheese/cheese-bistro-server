package com.ooadprojectserver.restaurantmanagement.repository.operation;

import com.ooadprojectserver.restaurantmanagement.model.operation.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operation, UUID> {
}
