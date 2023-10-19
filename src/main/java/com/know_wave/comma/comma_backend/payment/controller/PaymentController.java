package com.know_wave.comma.comma_backend.payment.controller;

import com.know_wave.comma.comma_backend.common.idempotency.IdempotentDto;
import com.know_wave.comma.comma_backend.payment.dto.*;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentGateway paymentGateway;

    @PostMapping("/api/v1/payment/ready")
    public PaymentAuthResponse readyPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                            @Valid @RequestBody PaymentRequest request) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/ready", request.toString());

        return paymentGateway.preparePayment(idempotentKeyDto, PaymentAuthRequest.of(request));
    }

    @GetMapping("/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}")
    public ResponseEntity<HttpHeaders> approvePayment(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @RequestParam("pg_token") String paymentToken) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey + paymentToken);

        paymentGateway.confirmPayment(idempotentKeyDto, paymentType, paymentRequestId, paymentToken);

        return WebEntityCreator.toRedirectEntity(URI.create("/orders"), HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/api/v1/payment/refund")
    public String refundPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                @Valid @RequestBody PaymentRefundRequest request) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/refund", request.toString());

        paymentGateway.refundPayment(idempotentKeyDto, request);

        return "Refunded deposit";
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

        paymentGateway.handlePaymentFailure(idempotentKeyDto, paymentType, paymentRequestId);

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

        paymentGateway.handlePaymentCancel(idempotentKeyDto, paymentType, paymentRequestId);

        return "Canceled payment process";
    }


}
