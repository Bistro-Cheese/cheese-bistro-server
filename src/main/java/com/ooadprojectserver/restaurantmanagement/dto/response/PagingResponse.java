package com.ooadprojectserver.restaurantmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse {
    private List<?> data;
    private long totalResult;
    private int totalPage;
    private int currentPage;
}