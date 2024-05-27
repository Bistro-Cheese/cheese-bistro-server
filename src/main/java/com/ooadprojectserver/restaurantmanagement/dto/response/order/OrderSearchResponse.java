package com.ooadprojectserver.restaurantmanagement.dto.response.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_TIME_FORMAT2;

@Data
public class OrderSearchResponse {
    private UUID id;

    private Integer tableId;

    private Integer tableNumber;

    private Integer numberOfCustomer;

    private BigDecimal subTotal;

    private BigDecimal discount;

    private Integer discountType;

    private BigDecimal discountValue;

    private BigDecimal total;

    private Integer status;

    private BigDecimal deposit;

    @JsonFormat(pattern = DATE_TIME_FORMAT2,timezone = DateTimeConstant.TIMEZONE)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT2)
    private Date cusIn;
}
