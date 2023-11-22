package com.ooadprojectserver.restaurantmanagement.dto.response.food;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@ToString
public class FoodResponse {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String description;
    private String category;
    private String image;
    private Integer status;
}
