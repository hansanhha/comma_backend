package com.know_wave.comma.comma_backend.payment.controller;

import com.know_wave.comma.comma_backend.common.idempotency.IdempotentDto;
import com.know_wave.comma.comma_backend.common.sse.SseEmitterService;
import com.know_wave.comma.comma_backend.order.entity.Subject;
import com.know_wave.comma.comma_backend.order.service.user.OrderService;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundRequest;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentGateway paymentGateway;
    private final OrderService orderService;
    private final SseEmitterService emitterService;

//    @PostMapping("/api/v1/payment/ready")
//    public PaymentAuthResponse preparePayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
//                                              @Valid @RequestBody PaymentRequest request) {
//
//        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/ready", request.toString());
//
//        return paymentGateway.prepare(idempotentKeyDto, PaymentPrepareDto.of(request));
//    }

    @GetMapping("/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}/{accountId}/{orderNumber}/{subject}/{emitterId}")
    public String approvePayment(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @PathVariable("accountId") String accountId,
            @PathVariable("orderNumber") String orderNumber,
            @PathVariable("subject") String subject,
            @PathVariable("emitterId") String emitterId,
            @RequestParam("pg_token") String paymentToken) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}/{orderNumber}/{subject}",
                paymentType + paymentRequestId + idempotencyKey + paymentToken + orderNumber + subject);

        Long id = paymentGateway.approve(idempotentKeyDto, paymentType, paymentRequestId, orderNumber, paymentToken);

        orderService.order(accountId, orderNumber, Subject.valueOf(subject.toUpperCase()));

        paymentGateway.postProcessing(id, orderNumber);

        emitterService.sendEvent(emitterId, "success", Map.of("orderNumber", orderNumber));

        return "Ordered";
    }

    @PostMapping("/api/v1/payment/refund")
    public String refundPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                @Valid @RequestBody PaymentRefundRequest request) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/refund", request.toString());

        paymentGateway.refund(idempotentKeyDto, request);

        return "Refunded deposit";
    }

    @GetMapping("/api/v1/payment/{paymentType}/fail/{paymentRequestId}/{idempotencyKey}/{orderNumber}/{emitterId}")
    public String failPayment(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @PathVariable("orderNumber") String orderNumber,
            @PathVariable("emitterId") String emitterId) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/fail/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey);

        paymentGateway.handlePaymentFailure(idempotentKeyDto, paymentType, paymentRequestId);

        emitterService.sendEvent(emitterId, "failure", Map.of("orderNumber", orderNumber));

        return "Failed payment process";
    }

    @GetMapping("/api/v1/payment/{paymentType}/cancel/{paymentRequestId}/{idempotencyKey}/{orderNumber}/{emitterId}")
    public String cancelPaymentProcess(
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @PathVariable("orderNumber") String orderNumber,
            @PathVariable("emitterId") String emitterId) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/cancel/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey);

        paymentGateway.handlePaymentCancel(idempotentKeyDto, paymentType, paymentRequestId);

        emitterService.sendEvent(emitterId, "canceled", Map.of("orderNumber", orderNumber));

        return "Canceled payment process";
    }


}
