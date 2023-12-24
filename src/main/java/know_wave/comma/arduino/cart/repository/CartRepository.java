package know_wave.comma.arduino.cart.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.component.entity.Arduino;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    @Query("select b from Cart b where b.arduino = :arduino and b.account = :account")
    Optional<Cart> findByArduinoAndAccount(Arduino arduino, Account account);

    @Query("select b from Cart b join fetch b.arduino where b.id = :storedId")
    Optional<Cart> findFetchArduinoById(Long storedId);

    @Query("select b from Cart b join fetch b.arduino where b.account = :account")
    List<Cart> findAllByAccount(Account account);
}
