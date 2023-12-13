package know_wave.comma.arduino.basket.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.basket.entity.Basket;
import know_wave.comma.arduino.component.entity.Arduino;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends CrudRepository<Basket, Long> {

    @Query("select b from Basket b where b.arduino = :arduino and b.account = :account")
    Optional<Basket> findByArduinoAndAccount(Arduino arduino, Account account);

    @Query("select b from Basket b join fetch b.arduino where b.id = :storedId")
    Optional<Basket> findFetchArduinoById(Long storedId);

    @Query("select b from Basket b join fetch b.arduino where b.account = :account")
    List<Basket> findAllByAccount(Account account);
}
