package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentReadyResponse;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.entity.*;
import com.know_wave.comma.comma_backend.payment.repository.PaymentReadyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentGateway {

    private final PaymentProvider<Payment, PaymentApprove> paymentProvider;
    private final PaymentReadyRepository paymentReadyRepository;
    public static final String successUrl = "http://localhost:8080/api/v1/payment/success";
    public static final String failUrl = "http://localhost:8080/api/v1/payment/fail";
    public static final String cancelUrl = "http://localhost:8080/api/v1/payment/cancel";

    public PaymentGateway(PaymentProvider<Payment, PaymentApprove> paymentProvider, PaymentReadyRepository paymentReadyRepository) {
        this.paymentProvider = paymentProvider;
        this.paymentReadyRepository = paymentReadyRepository;
    }

    public PaymentReadyResponse ready(PaymentRequest request, PaymentType type) {
        var paymentService = paymentProvider.getPaymentService(type);

        paymentProvider.getPaymentRequest(request, type);

        Payment ready = paymentService.ready(request.of());

        return PaymentReadyResponse.of(ready);
    }

    public void pay(PaymentRequest request, PaymentType type) {
        var paymentService = paymentProvider.getPaymentService(type);

        paymentService.pay(request.of());
    }

    public void refund(PaymentRequest request, PaymentType type) {
        var paymentService = paymentProvider.getPaymentService(type);

        paymentService.refund(request.of());
    }

    public void cancel(PaymentRequest request, PaymentType type) {
        var paymentService = paymentProvider.getPaymentService(type);

        paymentService.cancel(request.of());
    }

}
