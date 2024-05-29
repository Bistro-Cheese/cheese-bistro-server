package com.ooadprojectserver.restaurantmanagement.dto.request.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerCreateRequest {
    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("phone_number")
    private String phoneNumber;
}
