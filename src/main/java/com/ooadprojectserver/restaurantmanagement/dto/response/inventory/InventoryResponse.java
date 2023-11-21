package com.ooadprojectserver.restaurantmanagement.dto.response.inventory;

import lombok.Data;

import java.util.UUID;

@Data
public class InventoryResponse {
    private UUID id;
    private String ingredientName;
    private String supplier;
    private Double totalQuantity;
    private String unit;
}
