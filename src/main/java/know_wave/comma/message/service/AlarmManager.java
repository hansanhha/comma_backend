package know_wave.comma.message.service;

import jakarta.annotation.PostConstruct;
import know_wave.comma.message.dto.AlarmSendDto;
import know_wave.comma.message.dto.AlarmType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmManager {

    private final AlarmOptionCheckService alarmOptionCheckService;
    private final List<Sender> alarmNotifiers;
    private final AlarmHistoryService alarmHistoryService;
    private final Map<AlarmType, Class<? extends Sender>> notifierTypeMatcher = new HashMap<>();

    @PostConstruct
    public void init() {
        notifierTypeMatcher.put(AlarmType.WEB, WebSender.class);
        notifierTypeMatcher.put(AlarmType.KAKAOTALK, KakaotalkSender.class);
        notifierTypeMatcher.put(AlarmType.STUDENT_EMAIL, EmailSender.class);
    }

    public void sendAlarm(AlarmSendDto alarmSendDto) {
        if (alarmOptionCheckService.isAllowTime() && alarmOptionCheckService.isAllowFeature(alarmSendDto.alarmFeature())) {
            sendAlarmAllowedTypes(alarmSendDto);
            loggingAlarmHistory(alarmSendDto);
        }
    }

    private void sendAlarmAllowedTypes(AlarmSendDto alarmSendDto) {
        List<AlarmType> allowedAlarmTypes = alarmOptionCheckService.getAllowedTypes();

        allowedAlarmTypes.forEach(alarmType -> {
            Class<? extends Sender> alarmNotifierClassType = notifierTypeMatcher.get(alarmType);

            getAlarmNotifier(alarmNotifierClassType)
                    .send(alarmSendDto.dest(), alarmSendDto.title(), alarmSendDto.content());
        });
    }

    private void loggingAlarmHistory(AlarmSendDto alarmSendDto) {
        alarmHistoryService.logAccountHistory(alarmSendDto.title(), alarmSendDto.content());
        alarmHistoryService.logSystemHistory(alarmSendDto.title(), alarmSendDto.content());
    }

    private Sender getAlarmNotifier(Class<? extends Sender> alarmNotifierClassType) {
        return alarmNotifiers.stream()
                .filter(alarmNotifier -> alarmNotifier.getClass().equals(alarmNotifierClassType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 알람 타입을 찾을 수 없습니다."));
    }
}
