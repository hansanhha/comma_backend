package know_wave.comma.alarm.service;

import jakarta.annotation.PostConstruct;
import know_wave.comma.alarm.dto.AlarmSendDto;
import know_wave.comma.alarm.dto.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmManager {

    private final AlarmOptionCheckService alarmOptionCheckService;
    private final List<AlarmNotifier> alarmNotifiers;
    private final AlarmHistoryService alarmHistoryService;
    private final Map<AlarmType, Class<? extends AlarmNotifier>> notifierTypeMatcher = new HashMap<>();

    @PostConstruct
    public void init() {
        notifierTypeMatcher.put(AlarmType.WEB, WebNotifier.class);
        notifierTypeMatcher.put(AlarmType.KAKAOTALK, KakaotalkNotifier.class);
        notifierTypeMatcher.put(AlarmType.STUDENT_EMAIL, EmailNotifier.class);
    }

    public void sendAlarm(AlarmSendDto alarmSendDto) {
        if (alarmOptionCheckService.isAllowTime() && alarmOptionCheckService.isAllowFeature(alarmSendDto.feature())) {
            sendAlarmAllowedTypes(alarmSendDto);
            loggingAlarmHistory(alarmSendDto);
        }
    }

    private void sendAlarmAllowedTypes(AlarmSendDto alarmSendDto) {
        List<AlarmType> allowedAlarmTypes = alarmOptionCheckService.getAllowedTypes();

        allowedAlarmTypes.forEach(alarmType -> {
            Class<? extends AlarmNotifier> alarmNotifierClassType = notifierTypeMatcher.get(alarmType);

            getAlarmNotifier(alarmNotifierClassType)
                    .notify(alarmSendDto.title(), alarmSendDto.content(), alarmSendDto.link());
        });
    }

    private void loggingAlarmHistory(AlarmSendDto alarmSendDto) {
        alarmHistoryService.logAccountHistory(alarmSendDto.title(), alarmSendDto.content(), alarmSendDto.link());
        alarmHistoryService.logSystemHistory(alarmSendDto.title(), alarmSendDto.content());
    }

    private AlarmNotifier getAlarmNotifier(Class<? extends AlarmNotifier> alarmNotifierClassType) {
        return alarmNotifiers.stream()
                .filter(alarmNotifier -> alarmNotifier.getClass().equals(alarmNotifierClassType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 알람 타입을 찾을 수 없습니다."));
    }
}
