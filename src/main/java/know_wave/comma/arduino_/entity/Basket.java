package know_wave.comma.arduino_.entity;

import know_wave.comma.account.entity.Account;
import know_wave.comma.order_.entity.Order;
import know_wave.comma.order_.entity.OrderInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

@Entity
public class Basket {

    protected Basket() {}

    public Basket(Account account, Arduino arduino, int storedArduinoCount) {
        this.account = account;
        this.arduino = arduino;
        this.storedArduinoCount = storedArduinoCount;
    }

    @Id
    @GeneratedValue
    @Column(name = "basket_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    @Min(0)
    @Max(value = 10, message = "최대 10개까지 담을 수 있습니다.")
    private int storedArduinoCount;


    public static boolean isOverRequest(List<Basket> baskets) {
        return baskets.stream()
                .anyMatch(basket ->
                        basket.getArduino().isNotEnoughCount(basket.getStoredArduinoCount()));
    }

    public static List<Order> toOrders(List<Basket> baskets, OrderInfo orderInfo) {
        return baskets.stream()
                .map(basket -> Order.of(orderInfo, basket.getArduino(), basket.getStoredArduinoCount()))
                .toList();
    }

    public static boolean isEmpty(List<Basket> baskets) {
        return baskets.isEmpty();
    }

    public Arduino getArduino() {
        return arduino;
    }

    public int getStoredArduinoCount() {
        return storedArduinoCount;
    }

    public void setStoredArduinoCount(int count) {
        this.storedArduinoCount = count;
    }
}
