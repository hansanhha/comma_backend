package know_wave.comma.order.controller.user;

import jakarta.validation.Valid;
import know_wave.comma.common.idempotency.IdempotentDto;
import know_wave.comma.order.dto.*;
import know_wave.comma.order.service.user.OrderService;
import know_wave.comma.payment.dto.PaymentPrepareDto;
import know_wave.comma.payment.dto.PaymentPrepareResponse;
import know_wave.comma.payment.service.PaymentGateway;
import know_wave.comma.util.GenerateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentGateway paymentGateway;

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

    @PostMapping("/api/v2/order/arduino")
    public PaymentPrepareResponse orderUseSSE(@RequestHeader("Idempotency-Key") String idempotencyKey,
                                   @Valid @RequestBody OrderRequest orderRequest) {

        String orderNumber = GenerateUtils.generatedRandomCode();

        var idempotentKeyDto = new IdempotentDto(idempotencyKey, HttpMethod.POST.name(), "/api/v2/order/arduino", orderRequest.toString());

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
