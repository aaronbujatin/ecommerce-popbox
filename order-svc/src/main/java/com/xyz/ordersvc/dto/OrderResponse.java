package com.xyz.ordersvc.dto;

import com.xyz.ordersvc.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(
        Long id,
        BigDecimal totalAmount,
        Long userId,
        OrderStatus orderStatus,
        List<OrderItemResponse> orderItemResponseList
) {
}
