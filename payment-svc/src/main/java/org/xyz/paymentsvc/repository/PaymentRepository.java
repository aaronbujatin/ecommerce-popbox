package org.xyz.paymentsvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.xyz.paymentsvc.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {



}
