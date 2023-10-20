package com.ooadprojectserver.restaurantmanagement.dto.request;

import lombok.*;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {
    private String name;
    private Integer type;
}
