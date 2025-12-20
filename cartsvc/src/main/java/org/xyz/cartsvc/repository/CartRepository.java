package org.xyz.cartsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.cartsvc.entity.Cart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);


}
