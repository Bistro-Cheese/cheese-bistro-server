package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.discount.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscountRequest {
    private String name;
    @JsonProperty("discount_type")
    private DiscountType discountType;
    private BigDecimal value;
    @JsonProperty("uses_max")
    private Integer usesMax;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    @JsonProperty("is_active")
    private Boolean isActive;
}
