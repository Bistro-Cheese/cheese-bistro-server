package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.common.pagination.PageInfo;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventorySearchRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.inventory.InventoryUpdateRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.operation.OperationRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.inventory.InventoryResponse;
import com.ooadprojectserver.restaurantmanagement.model.inventory.Inventory;
import com.ooadprojectserver.restaurantmanagement.model.operation.Operation;
import com.ooadprojectserver.restaurantmanagement.service.inventory.InventoryService;
import com.ooadprojectserver.restaurantmanagement.service.operation.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.INVENTORY)
public class InventoryController {

    private final InventoryService inventoryService;
    private final OperationService operationService;

    @GetMapping()
    public ResponseEntity<Page<InventoryResponse>> searchInventory(InventorySearchRequest req, PageInfo pageInfo) {
        var rs = inventoryService.search(req, pageInfo);
        return ResponseEntity.status(HttpStatus.OK).body(rs);
    }

    @GetMapping(APIConstant.INVENTORY_ID)
    public ResponseEntity<APIResponse<Inventory>> getInventoryById(
            @PathVariable("inventory_id") UUID inventoryId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_INVENTORY_SUCCESS,
                        inventoryService.self(inventoryId)
                )
        );
    }

    @PutMapping(APIConstant.INVENTORY_ID)
    public ResponseEntity<MessageResponse> updateInventoryById(
            @PathVariable("inventory_id") UUID inventoryId,
            @RequestBody InventoryUpdateRequest request
            ) {
        inventoryService.update(inventoryId, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_INVENTORY_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.INVENTORY_ID)
    public ResponseEntity<MessageResponse> deleteInventoryById(
            @PathVariable("inventory_id") UUID inventoryId
    ) {
        inventoryService.delete(inventoryId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_INVENTORY_SUCCESS)
        );
    }

    @PostMapping(APIConstant.STOCK)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<MessageResponse> stockInventory(
            @RequestBody OperationRequest request
    ) {
        return ResponseEntity.ok(
                operationService.stockInventory(request)
        );
    }

    @GetMapping( APIConstant.OPERATIONS)
    public ResponseEntity<APIResponse<List<Operation>>> getOperations() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ALL_OPERATIONS_SUCCESS,
                        operationService.getAllOperation()
                )
        );
    }
}
