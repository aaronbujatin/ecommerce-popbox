package org.xyz.cartsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.cartsvc.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
