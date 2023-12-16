package know_wave.comma.common.notification.push.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.notification.push.repository.PushNotificationOptionRepository;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationOption;
import know_wave.comma.common.security.exception.NotSignInException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PushNotificationOptionReader {

    private final PushNotificationOptionRepository alarmOptionRepository;
    private final AccountQueryService accountQueryService;

    public boolean isSendableTime() {
        Account account = accountQueryService.findAccount();

        PushNotificationOption option = getAlarmOption(account);

        if (isNightTime()) {
            return option.isAlarmOn()
                    && option.isNightAlarmOn()
                    && isNotAlarmOffTime(option);
        }

        return option.isAlarmOn()
                && isNotAlarmOffTime(option);
    }

    public boolean isAllowFeature(NotificationFeature alarmFeature) {
        Account account = accountQueryService.findAccount();

        PushNotificationOption option = getAlarmOption(account);

        return option.isAllowFeature(alarmFeature);

    }

    public Set<PushNotificationType> getAllowedTypes() {
        Account account = accountQueryService.findAccount();

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

        return now.isBefore(option.getAlarmOffStartTime())
                || now.isAfter(option.getAlarmOffEndTime());
    }

    private boolean isNightTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() >= 22 || now.getHour() <= 6;
    }

    private PushNotificationOption getAlarmOption(Account account) {
        return alarmOptionRepository.findById(account).orElseThrow(() -> new NotSignInException("로그인이 되지 않아 알람을 보낼 수 없습니다."));
    }
}
