package com.ooadprojectserver.restaurantmanagement.dto.request.composition;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompositionRequest {
    private List<QuantityIngredientRequest> ingredients;
}

