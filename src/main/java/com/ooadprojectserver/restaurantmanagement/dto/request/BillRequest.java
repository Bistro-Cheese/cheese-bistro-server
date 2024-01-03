package com.ooadprojectserver.restaurantmanagement.dto.request;

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

    @JsonProperty("customer_id")
    private UUID customerId;

    @JsonProperty("discount_id")
    private Integer discountId;

    private BillStatus status;

    private BigDecimal paid;

    private BigDecimal deposit;


    // Payment
    @JsonProperty("method_id")
    private Integer methodId;
    @JsonProperty("payment_type")
    private PaymentType paymentType;
    @JsonProperty("customer_name")
    private String customerName;
    @JsonProperty("phone_number")
    private String phoneNumber;
}
