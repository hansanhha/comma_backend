package know_wave.comma.alarm.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.alarm.entity.AlarmOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmOptionRepository extends JpaRepository<AlarmOption, Account> {
}
