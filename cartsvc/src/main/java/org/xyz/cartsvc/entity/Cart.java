package org.xyz.cartsvc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.xyz.cartsvc.enums.CartStatus;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime convertedAt;


}
