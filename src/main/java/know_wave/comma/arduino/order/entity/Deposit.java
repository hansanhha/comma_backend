package know_wave.comma.arduino.order.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.payment.entity.Payment;
import lombok.*;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deposit extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "deposit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Setter
    @OneToOne(mappedBy = "deposit", fetch = FetchType.LAZY)
    private Order order;

    private int amount;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Setter
    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    public static Deposit create(Account account, int amount, Payment payment, DepositStatus depositStatus) {
        return new Deposit(null, account, null, amount, payment, depositStatus);
    }

}
