package com.know_wave.comma.comma_backend.payment.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@DiscriminatorValue("toss_pay")
@PrimaryKeyJoinColumn(name = "payment_response_id")
public class TossPaymentApprove extends PaymentApprove {
}
