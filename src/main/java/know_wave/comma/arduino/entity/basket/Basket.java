package know_wave.comma.arduino.entity.basket;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.entity.arduino.Arduino;

@Entity
public class Basket {

    @Id
    @GeneratedValue
    @Column(name = "basket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Arduino arduinoComponent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    private int storedArduinoCount;

}
