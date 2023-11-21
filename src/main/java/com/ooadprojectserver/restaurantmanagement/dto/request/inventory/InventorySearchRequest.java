package com.ooadprojectserver.restaurantmanagement.dto.request.inventory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InventorySearchRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("supplier")
    private String supplier;
}
