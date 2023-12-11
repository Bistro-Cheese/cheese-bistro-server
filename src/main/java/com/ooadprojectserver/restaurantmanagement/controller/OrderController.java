package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.DetailOrderResponse;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.ORDERS)
public class OrderController {
    private final OrderService orderService;

    @GetMapping(APIConstant.ORDER_BY_TABLE_ID)
    public ResponseEntity<APIResponse<DetailOrderResponse>> getOrderByTableId(
            @PathVariable("table_id") Integer tableId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ORDER_BY_TABLE_ID_SUCCESS,
                        orderService.getByTableId(tableId)
                )
        );
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        orderService.create(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(
                        MessageConstant.CREATE_ORDER_SUCCESS
                )
        );
    }

    @PutMapping()
    public ResponseEntity<MessageResponse> updateOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        orderService.update(orderRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(
                        MessageConstant.UPDATE_ORDER_SUCCESS
                )
        );
    }

    @DeleteMapping(APIConstant.ORDER_ID)
    public ResponseEntity<MessageResponse> deleteOrder(
            @PathVariable("order_id") UUID orderId
    ) {
        orderService.delete(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(
                        MessageConstant.DELETE_ORDER_SUCCESS
                )
        );
    }
}
