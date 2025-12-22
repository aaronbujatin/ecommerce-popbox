package org.xyz.cartsvc.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long id,
        BigDecimal totalPrice,
        List<CartItemResponse> cartItems
) {
}
