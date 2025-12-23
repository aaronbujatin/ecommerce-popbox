package org.xyz.cartsvc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "CART_ITEM")
public class CartItem {

    @Id
    @SequenceGenerator(
            name = "cart_item_seq",
            sequenceName = "cart_item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "cart_item_seq",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    private Long productId;
    private BigDecimal unitPrice;
    private int quantity;
    private BigDecimal totalPrice;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;


}