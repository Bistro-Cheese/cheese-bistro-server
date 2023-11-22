package com.ooadprojectserver.restaurantmanagement.dto.request.food;

import lombok.Data;

@Data
public class FoodSearchRequest {
    private String category;
    private String name;
    private String fromPrice;
    private String toPrice;
    private String sortCase;
    private Boolean isAscSort;
}
