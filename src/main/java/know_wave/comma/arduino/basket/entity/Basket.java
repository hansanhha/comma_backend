package know_wave.comma.arduino.basket.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.basket.dto.BasketValidateStatus;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoStockStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    public static BasketValidateStatus validate(Arduino arduino, int containQuantity, int maxQuantity) {
        ArduinoStockStatus stockStatus = arduino.getStockStatus();

        if (stockStatus == ArduinoStockStatus.NONE || stockStatus == ArduinoStockStatus.UPCOMMING) {
            return BasketValidateStatus.BAD_ARDUINO_STATUS;
        } else if (containQuantity > maxQuantity) {
            return BasketValidateStatus.OVER_MAX_QUANTITY;
        } else if (containQuantity > arduino.getCount()) {
            return BasketValidateStatus.NOT_ENOUGH_ARDUINO_STOCK;
        } else
            return BasketValidateStatus.VALID;
    }

    public static BasketValidateStatus validateList(List<Basket> basketList, int maxQuantity) {
        return basketList.stream()
                .map(detail -> validate(detail.getArduino(), detail.getStoredCount(), maxQuantity))
                .filter(orderStatus -> orderStatus != BasketValidateStatus.VALID)
                .findFirst()
                .orElse(BasketValidateStatus.VALID);
    }

    public void update(int count) {
        this.storedCount = count;
    }

}
