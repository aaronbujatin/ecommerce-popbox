package org.xyz.productsvc.dto;

import org.xyz.productsvc.enums.ProductUnitType;

import java.math.BigDecimal;

public record ProductUnitRequest(
        ProductUnitType productUnitType,
        BigDecimal price,
        String imageUrl,
        int stock
) {
}
