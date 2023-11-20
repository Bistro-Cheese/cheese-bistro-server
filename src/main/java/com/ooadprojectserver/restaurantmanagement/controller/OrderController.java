package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderResponse;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.ORDERS)
public class OrderController {
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<APIResponse<List<OrderResponse>>> getOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ORDER_SUCCESS,
                        orderService.getOrders()
                )
        );
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createOrder(
            @RequestBody OrderRequest orderRequest
    ) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(
                        MessageConstant.CREATE_ORDER_SUCCESS
                )
        );
    }

    @DeleteMapping(APIConstant.ORDER_ID)
    public ResponseEntity<MessageResponse> deleteOrder(
            @PathVariable("order_id") UUID orderId
    ) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(
                        MessageConstant.DELETE_ORDER_SUCCESS
                )
        );
    }
}
