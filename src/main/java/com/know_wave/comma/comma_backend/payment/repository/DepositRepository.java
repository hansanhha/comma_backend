package com.know_wave.comma.comma_backend.payment.repository;

import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
