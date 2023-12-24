package know_wave.comma.arduino.order.repository;

import know_wave.comma.arduino.order.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
