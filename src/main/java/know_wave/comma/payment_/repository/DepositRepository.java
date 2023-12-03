package know_wave.comma.payment_.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order_.entity.OrderInfo;
import know_wave.comma.payment_.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query("select d " +
            "from Deposit d " +
            "join fetch d.account " +
            "where d.paymentRequestId = :paymentRequestId")
    Optional<Deposit> fetchAccountByPaymentRequestId(String paymentRequestId);

    List<Deposit> findAllByAccountAndOrderInfo(Account account, OrderInfo orderInfo);
}
