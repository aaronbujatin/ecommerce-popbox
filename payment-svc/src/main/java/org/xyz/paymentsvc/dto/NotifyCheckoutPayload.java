package org.xyz.paymentsvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record NotifyCheckoutPayload(
        @JsonProperty("id")
        String id,
        @JsonProperty("status")
        String status,
        @JsonProperty("amount")
        BigDecimal amount,
        @JsonProperty("requestReferenceNumber")
        String requestReferenceNumber
) {
}
