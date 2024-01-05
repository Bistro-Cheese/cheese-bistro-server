package com.ooadprojectserver.restaurantmanagement.dto.response.order;

import com.ooadprojectserver.restaurantmanagement.model.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailOrderResponse {
    private UUID id;
    private StaffResponse staff;
    private List<OrderLineResponse> orderLines;
    private BigDecimal subTotal;
    private BigDecimal total;
    private OrderStatus status;
}
