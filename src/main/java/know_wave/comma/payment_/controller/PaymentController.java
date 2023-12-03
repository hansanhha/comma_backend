package know_wave.comma.payment_.controller;

import know_wave.comma.common.idempotency.IdempotentDto;
import know_wave.comma.common.sse.SseEmitterService;
import know_wave.comma.order_.entity.Subject;
import know_wave.comma.order_.service.user.OrderService;
import know_wave.comma.payment_.dto.PaymentRefundRequest;
import know_wave.comma.payment_.service.PaymentGateway;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}/{accountId}/{orderNumber}/{subject}/{sseId}")
    public String approvePayment(
            HttpServletRequest request,
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @PathVariable("accountId") String accountId,
            @PathVariable("orderNumber") String orderNumber,
            @PathVariable("subject") String subject,
            @PathVariable("sseId") String sseId,
            @RequestParam("pg_token") String paymentToken) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/success/{paymentRequestId}/{idempotencyKey}/{orderNumber}/{subject}",
                paymentType + paymentRequestId + idempotencyKey + paymentToken + orderNumber + subject);

        Long depositId = paymentGateway.approve(idempotentKeyDto, paymentType, paymentRequestId, orderNumber, paymentToken);

        orderService.order(accountId, orderNumber, Subject.valueOf(subject.toUpperCase()));

        paymentGateway.postProcessing(depositId, orderNumber);

        emitterService.sendEvent(request.getRemoteHost(), sseId, "success", orderNumber);

        return "Ordered";
    }

    @PostMapping("/api/v1/payment/refund")
    public String refundPayment(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                @Valid @RequestBody PaymentRefundRequest request) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/payment/refund", request.toString());

        paymentGateway.refund(idempotentKeyDto, request);

        return "Refunded deposit";
    }

    @GetMapping("/api/v1/payment/{paymentType}/fail/{paymentRequestId}/{idempotencyKey}/{orderNumber}/{sseId}")
    public String failPayment(
            HttpServletRequest request,
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @PathVariable("orderNumber") String orderNumber,
            @PathVariable("sseId") String sseId) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/fail/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey);

        paymentGateway.handlePaymentFailure(idempotentKeyDto, paymentType, paymentRequestId);

        emitterService.sendEvent(request.getRemoteHost(), sseId, "failure", orderNumber);

        return "Failed payment process";
    }

    @GetMapping("/api/v1/payment/{paymentType}/cancel/{paymentRequestId}/{idempotencyKey}/{orderNumber}/{sseId}")
    public String cancelPaymentProcess(
            HttpServletRequest request,
            @PathVariable("paymentType") String paymentType,
            @PathVariable("paymentRequestId") String paymentRequestId,
            @PathVariable("idempotencyKey") String idempotencyKey,
            @PathVariable("orderNumber") String orderNumber,
            @PathVariable("sseId") String sseId) {

        var idempotentKeyDto = new IdempotentDto(idempotencyKey,
                HttpMethod.GET.name(),
                "/api/v1/payment/{paymentType}/cancel/{paymentRequestId}/{idempotencyKey}",
                paymentType + paymentRequestId + idempotencyKey);

        paymentGateway.handlePaymentCancel(idempotentKeyDto, paymentType, paymentRequestId);

        emitterService.sendEvent(request.getRemoteHost(), sseId, "cancel", orderNumber);

        return "Canceled payment process";
    }


}
