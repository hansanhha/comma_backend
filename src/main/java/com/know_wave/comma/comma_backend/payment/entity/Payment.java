package com.know_wave.comma.comma_backend.payment.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public abstract class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "payment_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private OrderInfo orderInfo;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentReadyStatus;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    private int totalAmount;

    private String itemName = "컴마 실습재료 보증금";

    private boolean paymentTermsAgreement;

    private boolean personalInfoTermsAgreement;
}
