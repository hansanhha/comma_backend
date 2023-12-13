package know_wave.comma.notification.alarm.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmFeature {

    ACCOUNT("account"),
    ARDUINO("arduino"),
    COMMUNITY("community");

    final String name;
}
