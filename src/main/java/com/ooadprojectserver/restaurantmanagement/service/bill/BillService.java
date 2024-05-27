package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.dto.request.bill.BillRequest;
import com.ooadprojectserver.restaurantmanagement.dto.response.bill.BillResponse;
import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;

import java.util.List;
import java.util.UUID;

public interface BillService {
    Bill create(BillRequest billRequest);
    List<BillResponse> getBillByOrderId(UUID orderId);
}
