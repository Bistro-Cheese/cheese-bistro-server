package com.ooadprojectserver.restaurantmanagement.service.payment;

import com.ooadprojectserver.restaurantmanagement.dto.request.TransferMethodRequest;
import com.ooadprojectserver.restaurantmanagement.model.payment.TransferMethod;

import java.util.List;

public interface TransferMethodService {
    void create(TransferMethodRequest request);

    void update(Integer transferMethodId, TransferMethodRequest request);

    void delete(Integer transferMethodId);

    List<TransferMethod> getAll();

    TransferMethod getById(Integer transferMethodId);
}
