package com.ooadprojectserver.restaurantmanagement.service.operation;

import com.ooadprojectserver.restaurantmanagement.dto.request.operation.OperationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;

public interface OperationService {
    public MessageResponse stockInventory(OperationRequest req);
}
