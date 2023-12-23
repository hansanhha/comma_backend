package know_wave.comma.arduino.cart.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.cart.dto.CartValidateStatus;
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
public class Cart {

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

    public static Cart create(Account account, Arduino arduino, int count) {
        return new Cart(null, arduino, account, count);
    }

    public static CartValidateStatus validate(Arduino arduino, int containQuantity, int maxQuantity) {
        ArduinoStockStatus stockStatus = arduino.getStockStatus();

        if (stockStatus == ArduinoStockStatus.NONE || stockStatus == ArduinoStockStatus.UP_COMMING) {
            return CartValidateStatus.BAD_ARDUINO_STATUS;
        } else if (containQuantity > maxQuantity) {
            return CartValidateStatus.OVER_MAX_QUANTITY;
        } else if (containQuantity > arduino.getCount()) {
            return CartValidateStatus.NOT_ENOUGH_ARDUINO_STOCK;
        } else
            return CartValidateStatus.VALID;
    }

    public static CartValidateStatus validateList(List<Cart> cartList, int maxQuantity) {
        return cartList.stream()
                .map(detail -> validate(detail.getArduino(), detail.getStoredCount(), maxQuantity))
                .filter(orderStatus -> orderStatus != CartValidateStatus.VALID)
                .findFirst()
                .orElse(CartValidateStatus.VALID);
    }

    public void update(int count) {
        this.storedCount = count;
    }

}
