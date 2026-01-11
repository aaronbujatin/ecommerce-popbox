package com.xyz.ordersvc.service;

import com.xyz.ordersvc.dto.OrderCheckoutRequest;
import com.xyz.ordersvc.dto.OrderItemResponse;
import com.xyz.ordersvc.dto.OrderCheckoutResponse;
import com.xyz.ordersvc.enums.OrderStatus;
import java.util.List;


public interface OrderService {

    OrderCheckoutResponse checkoutOrder(OrderCheckoutRequest checkoutRequest);
    List<OrderItemResponse> filterOrderItems(Long userId, OrderStatus orderStatus);

}
