package com.ooadprojectserver.restaurantmanagement.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @JsonProperty("table_id")
    private Integer tableId;
    @JsonProperty("order_lines")
    private List<OrderLineRequest> orderLines;
}
