package com.ooadprojectserver.restaurantmanagement.dto.response.composition;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompositionResponse {
    private FoodInfoResponse food;
    private List<IngredientInfoResponse> ingredients;
}
