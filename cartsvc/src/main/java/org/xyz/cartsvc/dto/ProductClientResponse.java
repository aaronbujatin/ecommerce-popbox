package org.xyz.cartsvc.dto;

import java.math.BigDecimal;

public record ProductClientResponse(
        Long productId,
        int stock,
        BigDecimal price
) {
}
