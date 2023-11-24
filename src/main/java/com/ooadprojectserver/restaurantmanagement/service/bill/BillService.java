package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.dto.request.BillRequest;

public interface BillService {
    void create(BillRequest billRequest);
}
