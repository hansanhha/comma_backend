package know_wave.comma.notification.alarm.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AlarmType {

    WEB("web"),
    STUDENT_EMAIL("student_email"),
    KAKAOTALK("kakaotalk"),
    EMAIL("email");

    private final String name;
}
