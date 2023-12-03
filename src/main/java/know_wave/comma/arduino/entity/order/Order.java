package know_wave.comma.arduino.entity.order;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.payment_.entity.PaymentStatus;

@Entity
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private Subject subject;
}
