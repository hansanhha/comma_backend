package know_wave.comma.arduino.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.entity.Arduino;
import know_wave.comma.arduino.entity.Like;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<Like, Long> {

    Optional<Like> findByArduinoAndAccount(Arduino arduino, Account account);
}
