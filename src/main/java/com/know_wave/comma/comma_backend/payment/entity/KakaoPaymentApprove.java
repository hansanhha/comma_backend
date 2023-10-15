package com.know_wave.comma.comma_backend.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("kakao_pay")
@PrimaryKeyJoinColumn(name = "payment_response_id")
public class KakaoPaymentApprove extends PaymentApprove {

    private String aid;
    private String tid;
    private String paymentMethodType;
    private LocalDateTime approvedDate;
}
