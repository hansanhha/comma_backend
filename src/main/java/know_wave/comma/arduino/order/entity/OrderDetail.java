package know_wave.comma.arduino.order.entity;

import jakarta.persistence.*;
import know_wave.comma.arduino.basket.entity.Basket;
import know_wave.comma.arduino.component.entity.Arduino;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDetail {

    @Id
    @GeneratedValue
    @Column(name = "order_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arduino_id")
    private Arduino arduino;

    private int orderArduinoCount;

    public static OrderDetail create(Order order, Arduino arduino, int orderArduinoCount) {
        return new OrderDetail(null, order, arduino, orderArduinoCount);
    }

    public static List<OrderDetail> createOrderDetailList(Order order, List<Basket> basketList) {
        return basketList.stream()
                .map(basket -> OrderDetail.create(order, basket.getArduino(), basket.getStoredCount()))
                .toList();
    }
}
