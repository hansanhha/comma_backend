package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentWebResult;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentGateway {

    private final PaymentManager paymentManager;

    public static final String successUrl = "http://localhost:8080/api/v1/payment/success";
    public static final String failUrl = "http://localhost:8080/api/v1/payment/fail";
    public static final String cancelUrl = "http://localhost:8080/api/v1/payment/cancel";

    public PaymentWebResult ready(PaymentRequest request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

        PaymentWebResult result = paymentService.ready(request);

        return result;
    }

    public void pay(PaymentRequest request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

        paymentService.pay(request);
    }

    public void refund(PaymentRequest request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

        paymentService.refund(request);
    }

    public void cancel(PaymentRequest request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

        paymentService.cancel(request);
    }

}
