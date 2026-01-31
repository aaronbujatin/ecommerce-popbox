package com.xyz.ordersvc.service;

import com.xyz.ordersvc.client.CartClient;
import com.xyz.ordersvc.client.UserClient;
import com.xyz.ordersvc.dto.*;
import com.xyz.ordersvc.dto.external.ConvertProductReq;
import com.xyz.ordersvc.entity.Order;
import com.xyz.ordersvc.entity.OrderItem;
import com.xyz.ordersvc.enums.OrderStatus;
import com.xyz.ordersvc.enums.PaymentMode;
import com.xyz.ordersvc.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

    private final CartClient cartClient;
    private final OrderRepository orderRepository;
    private final UserClient userClient;

    @Override
    public OrderCheckoutResponse checkoutOrder(OrderCheckoutRequest checkoutRequest) {

        var extUserResp = userClient.getUserByById(checkoutRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException("User id not found"));


//        return null;
        var order = Order.builder()
                .paymentMode(PaymentMode.CASH_ON_DELIVERY)
                .build();

        cartClient.convertCart(new ExtCartConvertRequest(extUserResp.id(), checkoutRequest.cartItemIds()))
                .map(cart -> {
                    log.info("📦Status: {}, Message: {} and now on it's way.", OrderStatus.PROCESSING, OrderStatus.PROCESSING.getDescription());
                    cart.cartItems()
                        .forEach(cartItem -> {
                            var orderItem = OrderItem
                                .builder()
                                .productId(cartItem.productId())
                                .quantity(cartItem.quantity())
                                .unitPrice(cartItem.price())
                                .subTotal(cartItem.subTotal())
                                .build();
                            order.addOrderItem(orderItem);
                        });
                    order.setTotalAmount(cart.totalPrice());
                    order.setUserId(cart.userId());
                    order.setOrderStatus(OrderStatus.PROCESSING);
                   return order;
                })
                .orElseThrow();
       orderRepository.save(order);

        return new OrderCheckoutResponse(
                orderRepository.save(order).getId(),
                orderRepository.save(order).getUserId(),
                orderRepository.save(order).getTotalAmount(),
                orderItemResponseMapper(orderRepository.save(order).getOrderItems())
        );
    }

    @Override
    public List<OrderItemResponse> filterOrderItems(Long userId, OrderStatus orderStatus) {
//        return orderRepository.findByUserIdAndOrderStatus(userId, orderStatus)
//                .stream()
//                .
//                .flatMap(order -> order.getOrderItems().stream())
//                .map(orderItem -> new OrderItemResponse(
//                                orderItem.getId(),
//                                orderItem.getProductId(),
//                                orderItem.getUnitPrice(),
//                                orderItem.getSubTotal(),
//                                orderItem.getQuantity(),
//                                orderItem.getOrder().getOrderStatus()
//                        )
//                ).toList();
        return null;
    }

    private List<OrderItemResponse> orderItemResponseMapper(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> new OrderItemResponse(
                       orderItem.getId(),
                       orderItem.getProductId(),
                       orderItem.getUnitPrice(),
                       orderItem.getSubTotal(),
                       orderItem.getQuantity()
               )).toList();

    }


}
