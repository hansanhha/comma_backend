package know_wave.comma.alarm.service;

import jakarta.annotation.PostConstruct;
import know_wave.comma.alarm.dto.AlarmSendDto;
import know_wave.comma.alarm.dto.AlarmType;
import know_wave.comma.alarm.dto.MailSendDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmManager {

    private final AlarmOptionCheckService alarmOptionCheckService;
    private final List<AlarmSender> alarmNotifiers;
    private final AlarmHistoryService alarmHistoryService;
    private final Map<AlarmType, Class<? extends AlarmSender>> notifierTypeMatcher = new HashMap<>();

    @PostConstruct
    public void init() {
        notifierTypeMatcher.put(AlarmType.WEB, WebBrowserSender.class);
        notifierTypeMatcher.put(AlarmType.KAKAOTALK, KakaotalkSender.class);
        notifierTypeMatcher.put(AlarmType.STUDENT_EMAIL, EmailSender.class);
    }

    public void sendAlarm(AlarmSendDto alarmSendDto) {
        if (alarmOptionCheckService.isAllowTime() && alarmOptionCheckService.isAllowFeature(alarmSendDto.alarmFeature())) {
            sendAlarmAllowedTypes(alarmSendDto);
            loggingAlarmHistory(alarmSendDto);
        }
    }

    public void sendMail(MailSendDto mailSendDto) {
        getAlarmNotifier(EmailSender.class)
                .send(mailSendDto.destMail(), mailSendDto.title(), mailSendDto.content());
    }

    private void sendAlarmAllowedTypes(AlarmSendDto alarmSendDto) {
        List<AlarmType> allowedAlarmTypes = alarmOptionCheckService.getAllowedTypes();

        allowedAlarmTypes.forEach(alarmType -> {
            Class<? extends AlarmSender> alarmNotifierClassType = notifierTypeMatcher.get(alarmType);

            getAlarmNotifier(alarmNotifierClassType)
                    .send(alarmSendDto.dest(), alarmSendDto.title(), alarmSendDto.content());
        });
    }

    private void loggingAlarmHistory(AlarmSendDto alarmSendDto) {
        alarmHistoryService.logAccountHistory(alarmSendDto.title(), alarmSendDto.content());
        alarmHistoryService.logSystemHistory(alarmSendDto.title(), alarmSendDto.content());
    }

    private AlarmSender getAlarmNotifier(Class<? extends AlarmSender> alarmNotifierClassType) {
        return alarmNotifiers.stream()
                .filter(alarmNotifier -> alarmNotifier.getClass().equals(alarmNotifierClassType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 알람 타입을 찾을 수 없습니다."));
    }
}
