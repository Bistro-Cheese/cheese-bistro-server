package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderTableRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.model.order.OrderTable;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.TABLES)
public class OrderTableController {
    private final OrderTableService orderTableService;

    @PostMapping()
    public ResponseEntity<MessageResponse> createTable(
            @RequestBody OrderTableRequest req
    ) {
        orderTableService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.CREATE_TABLE_SUCCESS)
        );
    }

    @GetMapping()
    public ResponseEntity<APIResponse<List<OrderTable>>> getAllTables() {
        return ResponseEntity.ok(
                new APIResponse<>(MessageConstant.GET_TABLES_SUCCESS, orderTableService.getAll())
        );
    }

    @GetMapping(APIConstant.ID)
    public ResponseEntity<APIResponse<OrderTable>> getTableById(
            @PathVariable("id") Integer tableId
    ) {
        return ResponseEntity.ok(
                new APIResponse<>(MessageConstant.GET_TABLE_BY_ID_SUCCESS, orderTableService.getById(tableId))
        );
    }

    @PutMapping(APIConstant.ID)
    public ResponseEntity<MessageResponse> updateTable(
            @PathVariable("id") Integer tableId,
            @RequestBody OrderTableRequest req
    ) {
        orderTableService.update(tableId, req);
        return ResponseEntity.ok(
                new MessageResponse(MessageConstant.UPDATE_TABLE_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.ID)
    public ResponseEntity<MessageResponse> deleteTable(
            @PathVariable("id") Integer tableId
    ) {
        orderTableService.delete(tableId);
        return ResponseEntity.ok(
                new MessageResponse(MessageConstant.DELETE_TABLE_SUCCESS)
        );
    }
}
