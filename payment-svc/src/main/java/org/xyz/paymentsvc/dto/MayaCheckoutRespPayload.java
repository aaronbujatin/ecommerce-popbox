package org.xyz.paymentsvc.dto;

public record MayaCheckoutRespPayload(
        String checkoutId,
        String redirectUrl
) {
}
