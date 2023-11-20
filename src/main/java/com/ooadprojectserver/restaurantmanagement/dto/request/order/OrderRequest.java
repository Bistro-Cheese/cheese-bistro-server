package com.ooadprojectserver.restaurantmanagement.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @JsonProperty("table_id")
    private Integer tableId;
}
