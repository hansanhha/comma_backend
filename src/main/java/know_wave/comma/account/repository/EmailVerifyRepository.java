package know_wave.comma.account.repository;

import know_wave.comma.account.entity.EmailVerify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerifyRepository extends JpaRepository<EmailVerify, String> {

}
