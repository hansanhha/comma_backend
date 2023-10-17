package com.know_wave.comma.comma_backend.payment.repository;

import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query("select d " +
            "from Deposit d " +
            "join fetch d.account " +
            "join fetch d.orderInfo " +
            "where d.paymentRequestId = :paymentRequestId")
    Optional<Deposit> findByPaymentRequestId(String paymentRequestId);
}
