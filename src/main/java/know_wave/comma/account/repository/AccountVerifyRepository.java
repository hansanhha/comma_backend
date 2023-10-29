package know_wave.comma.account.repository;

import know_wave.comma.account.entity.AccountEmailVerify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountVerifyRepository extends JpaRepository<AccountEmailVerify, String> {

}
