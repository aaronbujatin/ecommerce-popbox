package com.xyz.ordersvc.dto;

import java.math.BigDecimal;
import java.util.List;

public record ExtCartResponse(
        Long id,
        Long userId,
        BigDecimal totalPrice,
        List<ExtCartItemResponse> cartItems
) {
}
