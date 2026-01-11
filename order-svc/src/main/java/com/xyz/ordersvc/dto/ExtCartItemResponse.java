package com.xyz.ordersvc.dto;

import java.math.BigDecimal;

public record ExtCartItemResponse(
        Long productId,
        String name,
        BigDecimal price,
        String image,
        int quantity,
        BigDecimal subTotal
) {
}
