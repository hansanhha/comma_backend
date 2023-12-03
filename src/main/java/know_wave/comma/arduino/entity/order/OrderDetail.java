package know_wave.comma.arduino.entity.order;

import jakarta.persistence.*;
import know_wave.comma.arduino.entity.arduino.Arduino;

@Entity
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
}
