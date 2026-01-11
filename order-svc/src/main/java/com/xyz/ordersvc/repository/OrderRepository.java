package com.xyz.ordersvc.repository;

import com.xyz.ordersvc.entity.Order;
import com.xyz.ordersvc.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);

}
