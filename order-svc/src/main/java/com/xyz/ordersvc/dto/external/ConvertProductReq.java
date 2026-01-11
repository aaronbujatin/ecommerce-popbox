package com.xyz.ordersvc.dto.external;

import java.util.List;

public record ConvertProductReq(
        Long productId,
        List<Long> productUnitIds
) {
}