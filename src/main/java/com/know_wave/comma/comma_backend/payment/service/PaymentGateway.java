package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.entity.PaymentReady;
import com.know_wave.comma.comma_backend.payment.entity.PaymentApprove;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import org.springframework.stereotype.Service;

@Service
public final class PaymentGateway {

    private final PaymentProvider<PaymentReady, PaymentApprove> paymentProvider;

    public PaymentGateway(PaymentProvider<PaymentReady, PaymentApprove> paymentProvider) {
        this.paymentProvider = paymentProvider;
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
