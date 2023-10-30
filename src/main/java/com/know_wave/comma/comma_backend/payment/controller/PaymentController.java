package com.know_wave.comma.comma_backend.payment.controller;

import com.know_wave.comma.comma_backend.common.idempotency.IdempotentDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentAuthResponse;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRequest;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentGateway paymentGateway;

    @PostMapping("/api/v1/payment/ready")
    public PaymentAuthResponse readyPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                            @Valid @RequestBody PaymentRequest request) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/ready", request.toString());

        return paymentGateway.ready(idempotentKeyDto, PaymentAuthRequest.of(request));
    }

    @PostMapping("/api/v1/payment/refund")
    public String refundPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                @Valid @RequestBody PaymentRefundRequest request) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/refund", request.toString());

        paymentGateway.refund(idempotentKeyDto, request);

        return "Refunded deposit";
    }

    @GetMapping("/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}")
    public String approvePayment(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @RequestParam("pg_token") String paymentToken) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey + paymentToken);

        paymentGateway.pay(idempotentKeyDto, paymentType, paymentRequestId, paymentToken);

        return "Paid deposit";
    }

    @GetMapping("/api/v1/payment/{paymentType}/fail/{paymentRequestId}/{idempotencyKey}")
    public String failPayment(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/fail/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey);

        paymentGateway.failProcess(idempotentKeyDto, paymentType, paymentRequestId);

        return "Failed payment process";
    }

    @GetMapping("/api/v1/payment/{paymentType}/cancel/{paymentRequestId}/{idempotencyKey}")
    public String cancelPaymentProcess(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/cancel/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey);

        paymentGateway.cancelProcess(idempotentKeyDto, paymentType, paymentRequestId);

        return "Canceled payment process";
    }


}
