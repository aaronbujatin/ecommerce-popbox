package org.xyz.cartsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.cartsvc.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByCartId(Long id);
    CartItem findByProductId(Long id);

}
