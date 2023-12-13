package know_wave.comma.notification.alarm.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.notification.alarm.repository.AlarmOptionRepository;
import know_wave.comma.notification.alarm.dto.AlarmType;
import know_wave.comma.notification.alarm.dto.AlarmFeature;
import know_wave.comma.notification.alarm.entity.AlarmOption;
import know_wave.comma.common.config.security.exception.NotSignInException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmOptionCheckService {

    private final AlarmOptionRepository alarmOptionRepository;
    private final AccountQueryService accountQueryService;

    public boolean isAllowTime() {
        Account account = accountQueryService.findAccount();

        if (isNightTime()) {
            return isAlarmActivate(account)
                    && isNightAlarmActivate(account)
                    && isNotAlarmOffTime(account);
        }

        return isAlarmActivate(account)
                && isNotAlarmOffTime(account);
    }

    public boolean isAllowFeature(AlarmFeature alarmFeature) {
        Account account = accountQueryService.findAccount();

        return isAllowFeature(account, alarmFeature);
    }

    public List<AlarmType> getAllowedTypes() {
        Account account = accountQueryService.findAccount();

        AlarmOption alarmOption = getAlarmOption(account);

        List<AlarmType> alarmTypes = new ArrayList<>();

        if (alarmOption.isWebAlarmOn()) {
            alarmTypes.add(AlarmType.WEB);
        }
        if (alarmOption.isKakaotalkAlarmOn()) {
            alarmTypes.add(AlarmType.KAKAOTALK);
        }
        if (alarmOption.isStudentEmailAlarmOn()) {
            alarmTypes.add(AlarmType.STUDENT_EMAIL);
        }

        return alarmTypes;
    }

    private boolean isAlarmActivate(Account account) {
        AlarmOption alarmOption = getAlarmOption(account);

        return alarmOption.isAlarmOn();
    }

    private boolean isNightAlarmActivate(Account account) {
        AlarmOption alarmOption = getAlarmOption(account);

        return alarmOption.isNightAlarmOn();
    }

    private boolean isNotAlarmOffTime(Account account) {
        AlarmOption alarmOption = getAlarmOption(account);

        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(alarmOption.getAlarmOffStartTime())
                || now.isAfter(alarmOption.getAlarmOffEndTime());
    }

    private LocalDateTime getAlarmOffTime(Account account) {
        AlarmOption alarmOption = getAlarmOption(account);

        return alarmOption.getAlarmOffEndTime();
    }

    private boolean isAllowFeature(Account account, AlarmFeature alarmFeature) {
        AlarmOption alarmOption = getAlarmOption(account);

        return alarmOption.isAlarmSpecificFeatureOn(alarmFeature.getName());
    }

    private boolean isNightTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.getHour() >= 22 || now.getHour() <= 6;
    }

    private AlarmOption getAlarmOption(Account account) {
        return alarmOptionRepository.findById(account).orElseThrow(() -> new NotSignInException("로그인이 되지 않아 알람을 보낼 수 없습니다."));
    }
}
