package org.xyz.cartsvc.dto;

import java.math.BigDecimal;

public record ProductClientResponse(
        Long productId,
        String name,
        int stock,
        String image,
        BigDecimal price
) {
}
