package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("spend")
    private BigDecimal spend;
}
