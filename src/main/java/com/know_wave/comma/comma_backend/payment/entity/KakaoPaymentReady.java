package com.know_wave.comma.comma_backend.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("kakao_pay")
@PrimaryKeyJoinColumn(name = "payment_request_id")
public class KakaoPaymentReady extends PaymentReady {

    private String tid;
    private String pgToken;
    private String nextRedirectAppUrl;
    private String nextRedirectMobileUrl;
    private String nextRedirectPcUrl;
    private String androidAppScheme;
    private String iosAppScheme;
    private LocalDateTime readyDate;
}
