package com.xyz.ordersvc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderStatus {

    PENDING("Order received, waiting for confirmation"),
    PROCESSING("Order is being packed"),
    SHIPPED("Order is on the way"),
    DELIVERED("Order delivered successfully"),
    COMPLETED("Customer completed the order"),
    CANCELED("Order was cancelled"),
    DISAPPROVED("Order was disapproved"),
    RETURNED("Order was returned");

    private final String description;

}
