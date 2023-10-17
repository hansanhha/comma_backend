package com.know_wave.comma.comma_backend.payment.repository;

import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query("select d " +
            "from Deposit d " +
            "where d.account = (select a from Account a where a.id = :accountId) " +
            "and d.orderInfo = (select o from OrderInfo o where o.orderNumber = :orderId )")
    Optional<Deposit> findByAccountIdAndOrderId(String accountId, String orderId);
}
