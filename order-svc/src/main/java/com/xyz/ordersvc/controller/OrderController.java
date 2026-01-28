package com.xyz.ordersvc.controller;

import com.xyz.ordersvc.dto.*;
import com.xyz.ordersvc.entity.Order;
import com.xyz.ordersvc.enums.OrderStatus;
import com.xyz.ordersvc.repository.OrderRepository;
import com.xyz.ordersvc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PostMapping("/checkout")
    public ResponseEntity<OrderCheckoutResponse> checkoutOrder(@RequestBody OrderCheckoutRequest orderCheckoutRequest) {

        return ResponseEntity.ok(orderService.checkoutOrder(orderCheckoutRequest));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> filterOrderItems(
                @RequestParam("userId") Long userId,
                @RequestParam("status") OrderStatus orderStatus
            ) {

        var orders = orderRepository.findByUserIdAndOrderStatus(userId, orderStatus)
                .stream()
                .map(order -> new OrderResponse(
                            order.getId(),
                            order.getTotalAmount(),
                            order.getUserId(),
                            order.getOrderStatus(),
                            order.getOrderItems()
                                    .stream()
                                    .map(orderItem -> new OrderItemResponse(
                                                orderItem.getId(),
                                                orderItem.getProductId(),
                                                orderItem.getUnitPrice(),
                                                orderItem.getSubTotal(),
                                                orderItem.getQuantity()
                                        )
                                    )
                                    .toList()
                    )
                )
                .toList();

        return ResponseEntity.ok(orders);
    }

}
