package com.ooadprojectserver.restaurantmanagement.service.payment.impl;

import com.ooadprojectserver.restaurantmanagement.constant.APIStatus;
import com.ooadprojectserver.restaurantmanagement.dto.request.TransferMethodRequest;
import com.ooadprojectserver.restaurantmanagement.exception.CustomException;
import com.ooadprojectserver.restaurantmanagement.model.payment.TransferMethod;
import com.ooadprojectserver.restaurantmanagement.repository.payment.TransferMethodRepository;
import com.ooadprojectserver.restaurantmanagement.service.payment.TransferMethodService;
import com.ooadprojectserver.restaurantmanagement.service.user.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ooadprojectserver.restaurantmanagement.util.DataUtil.copyProperties;

@Service
@RequiredArgsConstructor
public class TransferMethodServiceImpl implements TransferMethodService {
    private final TransferMethodRepository repository;
    private final UserDetailService userDetailService;

    @Override
    public void create(TransferMethodRequest request) {
        repository.findByAccountNumber(request.getAccountNumber()).ifPresent(
                transferMethod -> {
                    throw new CustomException(APIStatus.PAYMENT_METHOD_ALREADY_EXISTED);
                });
        TransferMethod newTransferMethod = copyProperties(request, TransferMethod.class);
        newTransferMethod.setCommonCreate(userDetailService.getIdLogin());
        repository.save(newTransferMethod);
    }

    @Override
    public void update(Integer transferMethodId, TransferMethodRequest req) {
        TransferMethod transferMethod = getTransferMethod(transferMethodId);
        transferMethod.setAccountNumber(req.getAccountNumber());
        transferMethod.setAccountHolderName(req.getAccountHolderName());
        transferMethod.setIsActive(req.getIsActive());
        transferMethod.setCommonUpdate(userDetailService.getIdLogin());
        repository.save(transferMethod);
    }

    @Override
    public void delete(Integer transferMethodId) {
        TransferMethod transferMethod = this.getById(transferMethodId);
        repository.delete(transferMethod);
    }

    @Override
    public List<TransferMethod> getAll() {
        Integer loginRole = userDetailService.getRoleLogin();
        if (loginRole == 2) {
            return repository.findByIsActive(true);
        }
        return repository.findAll();
    }

    @Override
    public TransferMethod getById(Integer transferMethodId) {
        return this.getTransferMethod(transferMethodId);
    }

    private TransferMethod getTransferMethod(Integer transferMethodId) {
        return repository.findById(transferMethodId).orElseThrow(
                () -> new CustomException(APIStatus.TRANSFER_METHOD_NOT_FOUND)
        );
    }
}
