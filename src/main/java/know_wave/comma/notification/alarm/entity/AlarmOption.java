package know_wave.comma.notification.alarm.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class AlarmOption extends BaseTimeEntity {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(nullable = false)
    private boolean alarmOn = false;

    @Column(nullable = false)
    private boolean nightAlarmOn = false;

    @Column
    private LocalDateTime alarmOffEndTime;

    @Column
    private LocalDateTime alarmOffStartTime;

    @Column(nullable = false)
    private boolean webAlarmOn = false;

    @Column(nullable = false)
    private boolean kakaotalkAlarmOn = false;

    @Column(nullable = false)
    private boolean studentEmailAlarmOn = false;

    @Column
    private boolean accountAlarmOn = false;

    @Column
    private boolean arduinoAlarmOn = false;

    @Column
    private boolean communityAlarmOn = false;

    public boolean isAlarmSpecificFeatureOn(String feature) {
        return switch (feature) {
            case "account" -> accountAlarmOn;
            case "arduino" -> arduinoAlarmOn;
            case "community" -> communityAlarmOn;
            default -> false;
        };
    }

}
