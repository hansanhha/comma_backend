package com.know_wave.comma.comma_backend.payment.controller;

import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResponse;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentGateway paymentGateway;

    @PostMapping("/api/v1/payment/ready")
    public PaymentAuthResponse readyPayment(@Valid @RequestBody PaymentRequest request) {
        return paymentGateway.ready(PaymentAuthRequest.of(request));
    }

    @GetMapping("/api/v1/payment/{paymentType}/success/{paymentRequestId}")
    public String approvePayment(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @RequestParam("pg_token") String paymentToken) {

        paymentGateway.pay(paymentType, paymentRequestId, paymentToken);

        return "Paid deposit";
    }
}
