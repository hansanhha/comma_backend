package know_wave.comma.account.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.config.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "select t from Token t join Account a on t.account = :account" +
            " where t.expired = false and t.revoked = false")
    List<Token> findAllByAccount(Account account);

    Optional<Token> findByToken(String token);
}
