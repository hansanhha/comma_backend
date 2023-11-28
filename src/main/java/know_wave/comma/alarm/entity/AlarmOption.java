package know_wave.comma.alarm.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
public class AlarmOption extends BaseTimeEntity {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    private boolean alarmOn = false;

    @Column
    private boolean nightAlarm = false;

    @Column(nullable = false)
    private LocalDateTime alarmOffTime;

    @Column
    private boolean webAlarm = false;

    @Column
    private boolean kakaotalkAlarm = false;

    @Column
    private boolean studentEmailAlarm = false;

    @Column(nullable = false)
    private String emailAlarm;

}
