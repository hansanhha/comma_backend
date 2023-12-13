package know_wave.comma.arduino.basket.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.component.entity.Arduino;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Basket {

    @Id
    @GeneratedValue
    @Column(name = "basket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    private int storedCount;

    public static Basket create(Account account, Arduino arduino, int count) {
        return new Basket(null, arduino, account, count);
    }

    public void update(int count) {
        this.storedCount = count;
    }

}
