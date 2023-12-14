package know_wave.comma.common.notification.push.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.common.notification.push.entity.PushNotificationOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PushNotificationOptionRepository extends JpaRepository<PushNotificationOption, Account> {
}
