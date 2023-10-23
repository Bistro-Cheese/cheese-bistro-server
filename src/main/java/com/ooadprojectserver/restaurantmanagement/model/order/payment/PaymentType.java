package com.ooadprojectserver.restaurantmanagement.model.order.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {
    CASH,
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    MOMO,
    GOOGLE_PAY,
    APPLE_PAY,
    OTHERS
}
