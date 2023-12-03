package know_wave.comma.arduino.entity.order;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.payment_.entity.PaymentStatus;
import know_wave.comma.payment_.entity.PaymentType;

@Entity
public class Deposit {

    @Id
    @GeneratedValue
    @Column(name = "deposit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    private String paymentRequestId;

    private String paymentTransactionId;
}
