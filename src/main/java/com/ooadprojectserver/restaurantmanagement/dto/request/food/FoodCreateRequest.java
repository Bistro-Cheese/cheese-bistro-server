package com.ooadprojectserver.restaurantmanagement.dto.request.food;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodCreateRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("category")
    private Integer category;
    @JsonProperty("description")
    private String description;
    @JsonProperty("product_image")
    private String productImage;
    @JsonProperty("price")
    private Long price;
    @JsonProperty("status")
    private Integer status;
}
