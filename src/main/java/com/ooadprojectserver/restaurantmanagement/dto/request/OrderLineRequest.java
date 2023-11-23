package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.UUID;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineRequest {
    @JsonProperty("order_id")
    private UUID orderId;
    @JsonProperty("food_id")
    private UUID foodId;
    private Integer quantity;
}
