package org.xyz.cartsvc.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long productId,
        String name,
        BigDecimal price,
        String image,
        int quantity,
        BigDecimal subTotal
) {
}