package com.ooadprojectserver.restaurantmanagement.dto.request.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InventoryUpdateRequest {
    @JsonProperty("total_quantity")
    private Double totalQuantity;
}
