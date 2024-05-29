package com.ooadprojectserver.restaurantmanagement.dto.response.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderLineResponse;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.DATE_TIME_FORMAT2;

@Data
public class BillResponse {
    private UUID id;
    private UUID orderId;
    private String customerName;
    private Integer numberOfCustomer;
    private Integer tableNumber;
    private Integer paymentType;
    private BigDecimal subTotal;
    private BigDecimal discountValue;
    private Integer discountType;
    private BigDecimal total;
    private BigDecimal deposit;
    private BigDecimal paid;
    private BigDecimal changePaid;

    private List<OrderLineResponse> orderLines;

    @JsonFormat(pattern = DATE_TIME_FORMAT2,timezone = DateTimeConstant.TIMEZONE)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT2)
    private Date cusIn;

    @JsonFormat(pattern = DATE_TIME_FORMAT2,timezone = DateTimeConstant.TIMEZONE)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT2)
    private Date cusOut;
}
