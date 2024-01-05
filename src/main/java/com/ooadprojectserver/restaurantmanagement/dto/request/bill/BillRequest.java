package com.ooadprojectserver.restaurantmanagement.dto.request.bill;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.bill.BillStatus;
import com.ooadprojectserver.restaurantmanagement.model.payment.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest {

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("status")
    private BillStatus status;

    @JsonProperty("paid")
    private BigDecimal paid;

    // Payment
    @JsonProperty("method_id")
    private Integer methodId;

    @JsonProperty("payment_type")
    private PaymentType paymentType;
}
