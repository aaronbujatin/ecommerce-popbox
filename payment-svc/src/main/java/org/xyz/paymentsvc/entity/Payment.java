package org.xyz.paymentsvc.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.xyz.paymentsvc.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue
    private Long id;
    private Long orderId;
    private Long transactionId;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private BigDecimal amount;
    @CreationTimestamp
    private LocalDateTime paidAt;
    private String stringifyNotifPayload;
}
