package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.dto.request.bill.BillRequest;

public interface BillService {
    void create(BillRequest billRequest);
    void update(BillRequest billRequest);
}
