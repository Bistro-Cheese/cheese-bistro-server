package com.ooadprojectserver.restaurantmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListFoodFilterRequest {
    @JsonProperty("category")
    private String category;
    @JsonProperty("search_key")
    private String searchKey;
    @JsonProperty("min_price")
    private BigDecimal minPrice;
    @JsonProperty("max_price")
    private BigDecimal maxPrice;
    //    private Integer bestSeller;
    @JsonProperty("sort_case")
    private int sortCase;
    @JsonProperty("is_asc_sort")
    private boolean isAscSort;

    @JsonProperty("page_number")
    private int pageNumber;
    @JsonProperty("page_size")
    private int pageSize;
}
