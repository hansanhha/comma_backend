package com.know_wave.comma.comma_backend.payment.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "payment_type")
public abstract class PaymentApprove extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "payment_response_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private OrderInfo orderInfo;

    @Enumerated(EnumType.STRING)
    private PaymentApproveStatus paymentApproveStatus;

}
