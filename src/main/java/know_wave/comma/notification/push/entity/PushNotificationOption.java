package know_wave.comma.notification.push.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.notification.base.entity.NotificationFeature;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PushNotificationOption extends BaseTimeEntity {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private boolean alarmOn = false;

    private boolean nightAlarmOn = false;

    private LocalDateTime alarmOffEndTime;

    private LocalDateTime alarmOffStartTime;

    private boolean webAlarmOn = false;

    private boolean kakaotalkAlarmOn = false;

    private boolean studentEmailAlarmOn = false;

    private boolean accountAlarmOn = false;

    private boolean arduinoCommentAlarmOn = false;

    private boolean arduinoOrderAlarmOn = false;

    private boolean arduinoRestockAlarmOn = false;

    private boolean communityAlarmOn = false;

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
