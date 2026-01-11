package com.xyz.ordersvc.dto;

import java.util.List;

public record CheckoutProductReq(
        Long productId,
        List<Long> productUnitIds
) {
}
