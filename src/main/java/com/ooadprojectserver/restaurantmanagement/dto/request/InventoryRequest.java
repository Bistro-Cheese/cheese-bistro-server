package com.ooadprojectserver.restaurantmanagement.dto.request;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequest {
    private Double quantity;
}
