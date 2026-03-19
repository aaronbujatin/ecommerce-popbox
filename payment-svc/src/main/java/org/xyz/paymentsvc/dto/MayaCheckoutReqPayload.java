package org.xyz.paymentsvc.dto;

import java.math.BigDecimal;

public record MayaCheckoutReqPayload(
        TotalAmount totalAmount,
        String requestReferenceNumber,
        RedirectUrl redirectUrl
) {
}

record TotalAmount(
        BigDecimal value,
        String currency
) {
}

record RedirectUrl(
       String success,
       String failure
){
}
