package com.ooadprojectserver.restaurantmanagement.service.payment.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.TransferMethodRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.repository.payment.PaymentRepository;
import com.ooadprojectserver.restaurantmanagement.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new CustomException(APIStatus.PAYMENT_NOT_FOUND)
        );
    }

    @Override
    public void create(TransferMethodRequest req) {

    }

    @Override
    public void update(UUID id, TransferMethodRequest req) {

    }

    @Override
    public void delete(UUID id) {

    }
}
