package com.ooadprojectserver.restaurantmanagement.dto.request.order;

import com.ooadprojectserver.restaurantmanagement.model.order.TableStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTableRequest {
    private Integer number;
    private Integer seats;
    private TableStatus status;
}
