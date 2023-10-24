package com.ooadprojectserver.restaurantmanagement.dto.response.composition;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FoodInfoResponse {
    private UUID id;
    private String name;
}
