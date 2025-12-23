package org.xyz.cartsvc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.xyz.cartsvc.enums.CartStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "CART")
public class Cart {

    @Id
    @SequenceGenerator(
            name = "cart_seq",
            sequenceName = "cart_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "cart_seq",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private CartStatus status;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private LocalDateTime convertedAt;
    @Builder.Default
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public void addToCart(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void removeToCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setCart(null);
    }

}
