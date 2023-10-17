package com.know_wave.comma.comma_backend.payment.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Deposit extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "deposit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private OrderInfo orderInfo;

    @Enumerated(EnumType.STRING)
    private DepositStatus depositStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    private int totalAmount;

    private String itemName;

    private boolean paymentTermsAgreement;

    private boolean personalInfoTermsAgreement;
}
