package com.ooadprojectserver.restaurantmanagement.service.bill;

import com.ooadprojectserver.restaurantmanagement.dto.request.bill.BillRequest;
import com.ooadprojectserver.restaurantmanagement.model.bill.Bill;

public interface BillService {
    Bill create(BillRequest billRequest);
    void update(BillRequest billRequest);
}
