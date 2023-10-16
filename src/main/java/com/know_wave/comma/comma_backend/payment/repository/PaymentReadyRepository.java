package com.know_wave.comma.comma_backend.payment.repository;

import com.know_wave.comma.comma_backend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentReadyRepository extends JpaRepository<Payment, Long> {
}
