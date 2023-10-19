package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.order.dto.OrderInfoDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareResult;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundResult;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;

public interface PaymentService {

    PaymentPrepareResult ready(String idempotencyKey, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfo);

    void pay(Deposit deposit, String tempOrderId, String paymentToken);

    PaymentRefundResult refund(Deposit request);

    void cancel(Deposit request);

    boolean supports(PaymentType type);
}
