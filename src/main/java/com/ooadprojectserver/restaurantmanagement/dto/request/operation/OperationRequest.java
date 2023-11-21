package com.ooadprojectserver.restaurantmanagement.dto.request.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.operation.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationRequest {

    @JsonProperty("inventory_id")
    private String inventoryId;

    @JsonProperty("type")
    private OperationType type;

    @JsonProperty("quantity")
    private Double quantity;
}
