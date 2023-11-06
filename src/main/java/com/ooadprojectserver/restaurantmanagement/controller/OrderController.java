package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderResponse;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<APIResponse<List<OrderResponse>>> getOrders(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ORDER_SUCCESS,
                        orderService.getOrders(request)
                )
        );
    }

    @PostMapping()
    public ResponseEntity<MessageResponse> createOrder(
            HttpServletRequest request,
            @RequestBody OrderRequest orderRequest
    ) {
        orderService.createOrder(request, orderRequest);
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

    @PostMapping(APIConstant.ORDER_ID)
    public ResponseEntity<MessageResponse> createOrderLine(
            @PathVariable("order_id") UUID orderId,
            @RequestBody OrderLineRequest orderLineRequest
    ) {
        orderService.createOrderLine(orderId, orderLineRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(
                        MessageConstant.CREATE_ORDER_LINE_SUCCESS
                )
        );
    }

    @PutMapping(APIConstant.ORDER_LINE_ID)
    public ResponseEntity<MessageResponse> updateOrderLine(
            @PathVariable("order_line_id") UUID orderLineId,
            @RequestBody OrderLineRequest orderLineRequest
    ) {
        orderService.updateOrderLine(orderLineId, orderLineRequest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(
                        MessageConstant.UPDATE_ORDER_LINE_SUCCESS
                )
        );
    }

    @DeleteMapping(APIConstant.ORDER_LINE_ID)
    public ResponseEntity<MessageResponse> deleteOrderLine(
            @PathVariable("order_line_id") UUID orderLineId
    ) {
        orderService.deleteOrderLine(orderLineId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(
                        MessageConstant.DELETE_ORDER_LINE_SUCCESS
                )
        );
    }
}
