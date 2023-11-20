package com.ooadprojectserver.restaurantmanagement.model.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    CASH,
    TRANSFER;
}
