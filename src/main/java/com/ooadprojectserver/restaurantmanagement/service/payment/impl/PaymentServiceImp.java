package com.ooadprojectserver.restaurantmanagement.service.payment.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.PaymentRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.payment.Payment;
import com.ooadprojectserver.restaurantmanagement.model.payment.PaymentType;
import com.ooadprojectserver.restaurantmanagement.model.payment.TransferMethod;
import com.ooadprojectserver.restaurantmanagement.repository.payment.PaymentRepository;
import com.ooadprojectserver.restaurantmanagement.service.payment.PaymentService;
import com.ooadprojectserver.restaurantmanagement.service.payment.TransferMethodService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;


@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {
    private final UserDetailService userDetailService;
    private final TransferMethodService transferMethodService;

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getById(UUID id) {
        return this.getPaymentById(id);
    }

    @Override
    public Payment create(PaymentRequest req) {
        Payment payment = new Payment();

        if (req.getPaymentType() == PaymentType.CASH) {
            payment.setPaymentType(PaymentType.CASH);
            payment.setMethodId(null);
        } else {
            payment.setPaymentType(PaymentType.TRANSFER);
            TransferMethod transferMethod = transferMethodService.getById(req.getMethodId());
            payment.setMethodId(transferMethod);
        }

        payment.setCommonCreate(userDetailService.getIdLogin());
        return paymentRepository.save(payment);
    }

    @Override
    public void update(UUID id, PaymentRequest req) {
        Payment payment = this.getPaymentById(id);
        payment.setCommonUpdate(userDetailService.getIdLogin());
        paymentRepository.save(payment);
    }

    @Override
    public void delete(UUID id) {
        Payment payment = this.getPaymentById(id);
        paymentRepository.delete(payment);
    }

    private Payment getPaymentById(UUID id) {
        return paymentRepository.findById(id).orElseThrow(
                () -> new CustomException(APIStatus.PAYMENT_NOT_FOUND)
        );
    }
}
