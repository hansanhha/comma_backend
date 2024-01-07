package know_wave.comma.arduino.order.repository;

import know_wave.comma.arduino.order.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query("select d from Deposit d join fetch d.order where d.payment.paymentRequestId = :paymentRequestId")
    Optional<Deposit> findDepositByPaymentRequestId(String paymentRequestId);
}
