package com.ooadprojectserver.restaurantmanagement.controller;

import com.ooadprojectserver.restaurantmanagement.constant.APIConstant;
import com.ooadprojectserver.restaurantmanagement.dto.request.BillRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.MessageResponse;
import com.ooadprojectserver.restaurantmanagement.service.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIConstant.BILLS)
public class BillController {
    private final BillService billService;
    @PostMapping()
    public ResponseEntity<MessageResponse> createBill(@RequestBody BillRequest billRequest) {
        billService.create(billRequest);
        return ResponseEntity.status(201).body(new MessageResponse("Create bill successfully"));
    }
}
