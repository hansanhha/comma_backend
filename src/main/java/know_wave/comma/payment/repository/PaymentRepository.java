package know_wave.comma.payment.repository;

import know_wave.comma.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("select p from Payment p join fetch p.account pa where p.paymentRequestId = :paymentRequestId")
    Optional<Payment> findByPaymentRequestId(String paymentRequestId);
}
