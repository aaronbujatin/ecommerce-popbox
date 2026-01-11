package com.xyz.ordersvc.dto;

import com.xyz.ordersvc.dto.external.ConvertProductReq;

import java.util.List;
import java.util.Set;

public record ExtCartConvertRequest(
        Long userId,
        List<Long> cartItemIds
) {
}
