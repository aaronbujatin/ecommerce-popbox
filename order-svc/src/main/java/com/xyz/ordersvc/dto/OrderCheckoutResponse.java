package com.xyz.ordersvc.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderCheckoutResponse(
        Long id,
        Long userId,
        BigDecimal totalAmount,
        List<OrderItemResponse> orderItemResponses
) {
}
