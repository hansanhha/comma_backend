package com.know_wave.comma.comma_backend.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("kakao_pay")
@PrimaryKeyJoinColumn(name = "payment_request_id")
public class KakaoPaymentReady extends PaymentReady {

}
