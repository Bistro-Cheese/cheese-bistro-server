package com.ooadprojectserver.restaurantmanagement.dto.response.composition;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientInfoResponse {
    private Long id;
    private String name;
    private Integer portion;
}
