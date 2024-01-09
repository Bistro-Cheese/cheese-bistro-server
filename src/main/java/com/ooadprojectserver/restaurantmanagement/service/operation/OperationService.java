package com.ooadprojectserver.restaurantmanagement.service.operation;

import com.ooadprojectserver.restaurantmanagement.dto.request.operation.OperationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.operation.Operation;

import java.util.List;

public interface OperationService {
    List<Operation> getAllOperation();
    MessageResponse stockInventory(OperationRequest req);
}
