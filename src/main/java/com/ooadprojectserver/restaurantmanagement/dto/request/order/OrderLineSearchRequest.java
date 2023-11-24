package com.ooadprojectserver.restaurantmanagement.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderLineSearchRequest {
    @JsonProperty("order_id")
    private UUID orderId;
}
