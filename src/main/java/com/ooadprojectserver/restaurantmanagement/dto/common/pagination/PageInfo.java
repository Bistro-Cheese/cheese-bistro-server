package com.ooadprojectserver.restaurantmanagement.dto.common.pagination;

import lombok.Data;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.isNullObject;

@Data
public class PageInfo{

    private Integer currentPage = 0;

    private Integer pageSize;

    public Integer getCurrentPage() {
        return isNullObject(currentPage) ? 0 : currentPage;
    }

    public Integer getPageSize() {
        return isNullObject(pageSize) ? Integer.MAX_VALUE : pageSize;
    }
}
