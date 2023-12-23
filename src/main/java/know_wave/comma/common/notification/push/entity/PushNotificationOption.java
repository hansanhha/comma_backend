package know_wave.comma.common.notification.push.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushNotificationOption extends BaseTimeEntity {

    public static PushNotificationOption create() {
        return new PushNotificationOption();
    }

    @Id
    @GeneratedValue
    @Column(name = "push_notification_option_id")
    private Long id;

    private boolean alarmOn = true;

    private boolean nightAlarmOn = false;

    private LocalDateTime alarmOffEndTime;

    private LocalDateTime alarmOffStartTime;

    private boolean webAlarmOn = true;

    private boolean kakaotalkAlarmOn = true;

    private boolean studentEmailAlarmOn = true;

    private boolean accountAlarmOn = true;

    private boolean arduinoCommentAlarmOn = true;

    private boolean arduinoOrderAlarmOn = true;

    private boolean arduinoRestockAlarmOn = true;

    private boolean communityAlarmOn = true;

    public boolean isAllowFeature(NotificationFeature notificationFeature) {
        return switch (notificationFeature) {
            case ACCOUNT -> accountAlarmOn;
            case ARDUINO_COMMENT -> arduinoCommentAlarmOn;
            case ARDUINO_ORDER -> arduinoOrderAlarmOn;
            case ARDUINO_RESTOCK -> arduinoRestockAlarmOn;
            case COMMUNITY -> communityAlarmOn;
            default -> false;
        };
    }

}
