package org.xyz.productsvc.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        List<String> images,
        int stock
) {
}
