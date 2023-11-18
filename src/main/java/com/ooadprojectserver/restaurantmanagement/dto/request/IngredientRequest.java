package com.ooadprojectserver.restaurantmanagement.dto.request;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class IngredientRequest {
    private String name;
    private Integer type;
    private String unit;
    private String supplier;
}
