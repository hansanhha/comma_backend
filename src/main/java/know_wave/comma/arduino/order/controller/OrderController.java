package know_wave.comma.arduino.order.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.order.dto.OrderRequest;
import know_wave.comma.arduino.order.dto.preProcessOrderResponse;
import know_wave.comma.arduino.order.service.OrderCommandService;
import know_wave.comma.arduino.order.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/*  TODO
      * 멱등성 기능 구현
 */
@RestController
@RequestMapping("/arduino")
@RequiredArgsConstructor
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderCommandService orderCommandService;

    private static final String MESSAGE = "msg";
    private static final String DATA = "body";

    @PostMapping("/order")
    public Map<String, Object> order(@Valid @RequestBody OrderRequest orderRequest, HttpEntity<String> httpHeaders) {
        List<String> idempotencyKey = httpHeaders.getHeaders().get("Idempotency-Key");
        preProcessOrderResponse preProcessOrderResponse = orderCommandService.preProcessOrder(orderRequest);
        return Map.of(MESSAGE, "required deposit payment", DATA, preProcessOrderResponse);
    }

    @PostMapping("/orders/{orderNumber}/cancel")
    public Map<String, String> cancelOrder(@PathVariable String orderNumber) {
        orderCommandService.orderCancel(orderNumber);
        return Map.of(MESSAGE, "canceled order");
    }

    @GetMapping("/orders")
    public Map<String, Object> getOrderPage(Pageable pageable) {
        return Map.of(MESSAGE, "orders", DATA, orderQueryService.getOrderPage(pageable));
    }

    @GetMapping("/orders/{orderNumber}")
    public Map<String, Object> getOrderDetail(@PathVariable String orderNumber) {
        return Map.of(MESSAGE, "order detail", DATA, orderQueryService.getOrderDetail(orderNumber));
    }


}
