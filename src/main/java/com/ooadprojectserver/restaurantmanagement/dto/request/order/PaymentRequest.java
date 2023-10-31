package com.ooadprojectserver.restaurantmanagement.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    @JsonProperty("payment_type")
    private Integer paymentType;
}
