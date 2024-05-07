package com.ooadprojectserver.restaurantmanagement.dto.request.food;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FoodCreateRequest {
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty("category")
    @NotBlank(message = "Category is required")
    private Integer category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("image")
    private String productImage;

    @JsonProperty("price")
    @NotBlank(message = "Price is required")
    private BigDecimal price;

    @JsonProperty("status")
    private Integer status;
}
