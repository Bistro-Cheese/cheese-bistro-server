package com.ooadprojectserver.restaurantmanagement.model.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MethodType {
    BANK_TRANSFER,
    DIGITAL_WALLET
}
