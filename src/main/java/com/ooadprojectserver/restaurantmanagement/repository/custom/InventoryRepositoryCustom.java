package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventorySearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.inventory.InventoryResponse;
import org.springframework.data.domain.Page;

public interface InventoryRepositoryCustom {
    Page<InventoryResponse> search(InventorySearchRequest request, PageInfo pageInfo);
}
