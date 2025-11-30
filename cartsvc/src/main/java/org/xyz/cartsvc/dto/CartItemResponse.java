package org.xyz.cartsvc.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        int quantity,
        BigDecimal totalPrice
) {
}