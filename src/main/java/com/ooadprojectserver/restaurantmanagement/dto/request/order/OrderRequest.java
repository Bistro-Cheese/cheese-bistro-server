package com.ooadprojectserver.restaurantmanagement.dto.request.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.constant.DateTimeConstant;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.List;

import static com.ooadprojectserver.restaurantmanagement.util.DateTimeUtils.*;

@Data
public class OrderRequest {

    @JsonProperty("table_id")
    private Integer tableId;

    @JsonProperty("customer_name")
    private String customerName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("number_of_customer")
    private Integer numberOfCustomer;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("discount_id")
    private Integer discountId;

    @JsonProperty("cus_in")
    @JsonFormat(pattern = DATE_TIME_FORMAT2,timezone = DateTimeConstant.TIMEZONE)
    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Timestamp cusIn;

    @JsonProperty("order_lines")
    private List<OrderLineRequest> orderLines;
}
