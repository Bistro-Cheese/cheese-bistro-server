package com.ooadprojectserver.restaurantmanagement.dto.request.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.operation.OperationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OperationRequest {

    @JsonProperty("inventory_id")
    private UUID inventoryId;

    @JsonProperty("type")
    @Enumerated(EnumType.ORDINAL)
    private OperationType type;

    @JsonProperty("quantity")
    private Double quantity;
}
