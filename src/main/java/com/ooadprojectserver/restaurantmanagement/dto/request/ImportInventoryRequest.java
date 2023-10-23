package com.ooadprojectserver.restaurantmanagement.dto.request;

import lombok.*;

@Data
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImportInventoryRequest {
    private Integer quantity;
}
