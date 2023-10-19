package com.know_wave.comma.comma_backend.order.controller.user;

import com.know_wave.comma.comma_backend.common.idempotency.IdempotentDto;
import com.know_wave.comma.comma_backend.common.sse.SseEmitterService;
import com.know_wave.comma.comma_backend.order.dto.*;
import com.know_wave.comma.comma_backend.order.service.user.OrderService;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareResponse;
import com.know_wave.comma.comma_backend.payment.service.PaymentGateway;
import com.know_wave.comma.comma_backend.util.GenerateUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentGateway paymentGateway;
    private final SseEmitterService sseEmitterService;

    @PostMapping("/api/test/order/arduino")
    public String order(@Valid @RequestBody OrderRequest request) {
        orderService.order(request.getSubject());
        return "Ordered";
    }

    @PostMapping("/api/v1/order/arduino")
    public PaymentPrepareResponse order(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                        @Valid @RequestBody OrderRequest request) {

        String orderNumber = GenerateUtils.generatedRandomCode();

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/order/arduino", request.toString());

        return paymentGateway.prepare(idempotentKeyDto,
                new PaymentPrepareDto(orderNumber, request.getPaymentType()),
                new OrderInfoDto(request.getSubject()));
    }

    @GetMapping(value = "/sse/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connectSse() {
        return sseEmitterService.create();
    }

    @PostMapping("/api/v2/order/arduino")
    public PaymentPrepareResponse orderUseSSE(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                   @Valid @RequestBody OrderRequest orderRequest) {

        String orderNumber = GenerateUtils.generatedRandomCode();

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v1/order/arduino", orderRequest.toString());

         return paymentGateway.prepareWithSSE(
                 idempotentKeyDto,
                 new PaymentPrepareDto(orderNumber, orderRequest.getPaymentType()),
                 new OrderInfoDto(orderRequest.getSubject()),
                 orderRequest.getSseId()
         );
    }

    @PostMapping("/orders/{orderCode}/arduino")
    public String addOrder(@PathVariable("orderCode") String orderCode,
                           @Valid @RequestBody OrderMoreRequest request) {
        orderService.moreOrder(orderCode, request);
        return "Added order";
    }

    @GetMapping("/orders")
    public List<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/orders/{orderCode}")
    public OrderDetailResponse getOrderDetail(@PathVariable("orderCode") String code) {
        return orderService.getOrderDetail(code);
    }

    @PatchMapping("/orders/{orderCode}/cancel_request")
    public String cancelOrderRequest(@PathVariable("orderCode") String code, @Valid @RequestBody OrderCancelRequest request) {
        orderService.cancelOrderRequest(code, request);
        return "Requested order cancellation";
    }
}
