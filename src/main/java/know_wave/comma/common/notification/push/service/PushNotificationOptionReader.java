package know_wave.comma.common.notification.push.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationOption;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import know_wave.comma.common.notification.push.repository.PushNotificationOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
class PushNotificationOptionReader {

    private final PushNotificationOptionRepository alarmOptionRepository;

    public boolean isSendableTime(Account account) {
        PushNotificationOption option = getAlarmOption(account);

        if (isNightTime()) {
            return option.isAlarmOn()
                    && !option.isNightAlarmOn()
                    && isNotAlarmOffTime(option);
        }

        return option.isAlarmOn()
                && isNotAlarmOffTime(option);
    }

    public boolean isAllowFeature(Account account, NotificationFeature alarmFeature) {
        PushNotificationOption option = getAlarmOption(account);

        return option.isAllowFeature(alarmFeature);

    }

    public Set<PushNotificationType> getAllowedTypes(Account account) {
        PushNotificationOption option = getAlarmOption(account);

        Set<PushNotificationType> allowTypes = new HashSet<>();

        if (option.isWebAlarmOn()) {
            allowTypes.add(PushNotificationType.WEB);
        }
        if (option.isKakaotalkAlarmOn()) {
            allowTypes.add(PushNotificationType.KAKAOTALK);
        }
        if (option.isStudentEmailAlarmOn()) {
            allowTypes.add(PushNotificationType.EMAIL);
        }

        return allowTypes;
    }

    private boolean isNotAlarmOffTime(PushNotificationOption option) {
        LocalDateTime now = LocalDateTime.now();

        if (option.getAlarmOffStartTime() == null || option.getAlarmOffEndTime() == null) {
            return true;
        }

        return now.isBefore(option.getAlarmOffStartTime())
                || now.isAfter(option.getAlarmOffEndTime());
    }

    private boolean isNightTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() >= 22 || now.getHour() <= 6;
    }

    private PushNotificationOption getAlarmOption(Account account) {
        return account.getNotificationOption();
    }
}
