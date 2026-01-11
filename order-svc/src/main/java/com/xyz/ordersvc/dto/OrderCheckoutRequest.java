package com.xyz.ordersvc.dto;

import java.util.List;
import java.util.Set;

public record OrderCheckoutRequest(
        Long userId,
        String paymentMode,
        List<Long> cartItemIds
) {
}
