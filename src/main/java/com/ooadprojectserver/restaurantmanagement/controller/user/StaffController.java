package com.ooadprojectserver.restaurantmanagement.controller.user;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.OrderRequest;
import com.ooadprojectserver.restaurantmanagement.dto.request.order.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.order.OrderResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.schedule.StaffScheduleResponse;
import com.ooadprojectserver.restaurantmanagement.model.order.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.service.payment.PaymentService;
import com.ooadprojectserver.restaurantmanagement.service.user.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.STAFF)
@PreAuthorize("hasRole('STAFF')")
public class StaffController {
    private final StaffService staffService;
    private final PaymentService paymentService;

    @GetMapping(APIConstant.SCHEDULE)
    public ResponseEntity<APIResponse<StaffScheduleResponse>> getSchedule(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_SCHEDULE_SUCCESS,
                        staffService.getSchedule(request)
                )
        );
    }

    @GetMapping(APIConstant.ORDER)
    public ResponseEntity<APIResponse<List<OrderResponse>>> getOrders(
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.GET_ORDER_SUCCESS,
                        staffService.getOrders(request)
                )
        );
    }

    @PostMapping(APIConstant.ORDER)
    public ResponseEntity<MessageResponse> createOrder(
            HttpServletRequest request,
            @RequestBody OrderRequest orderRequest
    ) {
        staffService.createOrder(request, orderRequest);
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
        staffService.deleteOrder(orderId);
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
        staffService.createOrderLine(orderId, orderLineRequest);
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
        staffService.updateOrderLine(orderLineId, orderLineRequest);
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
        staffService.deleteOrderLine(orderLineId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(
                        MessageConstant.DELETE_ORDER_LINE_SUCCESS
                )
        );
    }

    @PostMapping(APIConstant.ORDER_ID + APIConstant.PAY)
    public ResponseEntity<APIResponse<Payment>> createBill(
            @PathVariable("order_id") UUID orderId,
            @RequestBody PaymentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new APIResponse<>(
                        MessageConstant.CREATE_BILL_SUCCESS,
                        paymentService.createBill(request, orderId)
                )
        );
    }
}
