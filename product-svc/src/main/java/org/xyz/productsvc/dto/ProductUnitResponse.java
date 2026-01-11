package org.xyz.productsvc.dto;

import org.xyz.productsvc.enums.ProductUnitType;

import java.math.BigDecimal;

public record ProductUnitResponse(
        Long id,
        ProductUnitType unitType,
        BigDecimal price,
        int stock,
        String imageUrl
) {
}
