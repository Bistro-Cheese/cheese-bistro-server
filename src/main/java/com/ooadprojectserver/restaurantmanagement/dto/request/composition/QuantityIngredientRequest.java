package com.ooadprojectserver.restaurantmanagement.dto.request.composition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuantityIngredientRequest {
    @JsonProperty("ingredient_id")
    private Long ingredientId;
    private Integer quantity;
}
