package com.ooadprojectserver.restaurantmanagement.service.inventory;

import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventoryCreateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventorySearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventoryUpdateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.inventory.InventoryResponse;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface InventoryService {
    void create(InventoryCreateRequest req);
    public Page<InventoryResponse> search(InventorySearchRequest request, PageInfo pageInfo);
    void update(UUID id, InventoryUpdateRequest req);
    void delete(UUID id);
    Inventory self(UUID id);
}
