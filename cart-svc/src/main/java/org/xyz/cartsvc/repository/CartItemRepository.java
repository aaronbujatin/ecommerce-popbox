package org.xyz.cartsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.cartsvc.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long id);
    Optional<CartItem> findByProductId(Long id);

    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

}
