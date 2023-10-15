package com.know_wave.comma.comma_backend.payment.entity;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;
import com.know_wave.comma.comma_backend.util.entity.BaseTimeEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ArduinoDeposit extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne
    private OrderInfo orderInfo;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private int totalAmount;

    private String paymentNumber;

    private LocalDateTime paymentRequestedDate;

    private LocalDateTime paymentApprovedDate;
}
