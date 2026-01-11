package com.xyz.ordersvc.dto;

import com.xyz.ordersvc.enums.OrderStatus;

import java.math.BigDecimal;

public record OrderItemResponse (
            Long id,
            Long productId,
            BigDecimal unitPrice,
            BigDecimal subTotal,
            int quantity
){}
