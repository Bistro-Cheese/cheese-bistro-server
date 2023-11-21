package com.ooadprojectserver.restaurantmanagement.dto.request.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InventoryCreateRequest {
    @JsonProperty("ingredient_id")
    private Long ingredientId;
    @JsonProperty("total_quantity")
    private Double totalQuantity;
}
