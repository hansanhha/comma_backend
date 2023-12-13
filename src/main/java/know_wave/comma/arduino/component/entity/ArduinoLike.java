package know_wave.comma.arduino.component.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;

@Entity
public class ArduinoLike {

    @Id
    @GeneratedValue
    @Column(name = "arduino_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduinoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account accountId;

}
