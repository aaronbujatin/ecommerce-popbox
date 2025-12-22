package org.xyz.cartsvc.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductClientResponse(
        Long id,
        String name,
        int stock,
        List<String> images,
        BigDecimal price
) {
}
