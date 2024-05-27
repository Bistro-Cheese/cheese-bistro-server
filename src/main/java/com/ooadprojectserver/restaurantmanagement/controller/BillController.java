package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.bill.BillRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.APIResponse;
import com.ooadprojectserver.restaurantmanagement.dto.response.bill.BillResponse;
import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;
import com.ooadprojectserver.restaurantmanagement.service.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.BILLS)
public class BillController {
    private final BillService billService;
    @PostMapping()
    public ResponseEntity<APIResponse<Bill>> createBill(@RequestBody BillRequest billRequest) {
        var rs = billService.create(billRequest);
        return ResponseEntity.status(201).body(new APIResponse("Create bill successfully", rs));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<APIResponse<List<BillResponse>>> getBillByOrderId(@PathVariable UUID orderId) {
        var rs = billService.getBillByOrderId(orderId);
        return ResponseEntity.ok(new APIResponse("Get bill successfully", rs));
    }
}
