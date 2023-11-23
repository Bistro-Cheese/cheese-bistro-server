package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.constant.MessageConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.OrderLineRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.order.OrderLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.ORDER_LINES)
public class OrderLineController {
    private final OrderLineService orderLineService;

    @PostMapping()
    public ResponseEntity<MessageResponse> createOrderLine(@RequestBody OrderLineRequest req) {
        orderLineService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new MessageResponse(MessageConstant.CREATE_ORDER_LINE_SUCCESS)
        );
    }

    @PutMapping(APIConstant.ID)
    public ResponseEntity<MessageResponse> updateOrderLine(
            @PathVariable("id") UUID orderLineId,
            @RequestBody OrderLineRequest req
    ) {
        orderLineService.update(orderLineId, req);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.UPDATE_ORDER_LINE_SUCCESS)
        );
    }

    @DeleteMapping(APIConstant.ID)
    public ResponseEntity<MessageResponse> deleteOrderLine(
            @PathVariable("id") UUID id
    ) {
        orderLineService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MessageResponse(MessageConstant.DELETE_ORDER_LINE_SUCCESS)
        );
    }
}
