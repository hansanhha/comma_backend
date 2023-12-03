package know_wave.comma.arduino.entity.arduino;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;

@Entity
public class ArduinoRestockAlarm {

    @Id
    @GeneratedValue
    @Column(name = "arduino_restock_alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
}
