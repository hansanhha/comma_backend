package com.know_wave.comma.comma_backend.payment.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Deposit extends BaseTimeEntity {

    public Deposit(Account account, OrderInfo orderInfo, String paymentRequestId, String paymentTransactionId, DepositStatus depositStatus, PaymentStatus paymentStatus, PaymentType paymentType, PaymentMethodType paymentMethodType, int totalAmount, String itemName, boolean paymentTermsAgreement, boolean personalInfoTermsAgreement) {
        this.account = account;
        this.orderInfo = orderInfo;
        this.paymentRequestId = paymentRequestId;
        this.paymentTransactionId = paymentTransactionId;
        this.depositStatus = depositStatus;
        this.paymentStatus = paymentStatus;
        this.paymentType = paymentType;
        this.paymentMethodType = paymentMethodType;
        this.totalAmount = totalAmount;
        this.itemName = itemName;
        this.paymentTermsAgreement = paymentTermsAgreement;
        this.personalInfoTermsAgreement = personalInfoTermsAgreement;
    }

    @Id
    @GeneratedValue
    @Column(name = "deposit_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_info_id")
    private OrderInfo orderInfo;

    private String paymentRequestId;
    private String paymentTransactionId;

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

    private LocalDateTime refundedDate;

    private boolean paymentTermsAgreement;

    private boolean personalInfoTermsAgreement;

    public void setDepositStatus(DepositStatus depositStatus) {
        this.depositStatus = depositStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setRefundedDate(LocalDateTime refundedDate) {
        this.refundedDate = refundedDate;
    }
}
