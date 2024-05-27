package com.ooadprojectserver.restaurantmanagement.repository.custom;

import com.ooadprojectserver.restaurantmanagement.dto.response.bill.BillResponse;

import java.util.List;
import java.util.UUID;

public interface BillRepositoryCustom {
    List<BillResponse> getBillByOrderId(UUID orderId);
}
