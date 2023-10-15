package know_wave.comma.order.controller.admin;

import know_wave.comma.order.dto.OrderDetailResponse;
import know_wave.comma.order.dto.OrderResponse;
import know_wave.comma.order.dto.OrderStatusUpdateRequest;
import know_wave.comma.order.service.admin.OrderAdminService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class OrderAdminController {

    private final OrderAdminService orderAdminService;

    public OrderAdminController(OrderAdminService orderAdminService) {
        this.orderAdminService = orderAdminService;
    }

    @GetMapping("/orders")
    public List<OrderResponse> getAllOrders(Pageable pageable) {
        return orderAdminService.getOrders(pageable);
    }

    @GetMapping("/orders/apply")
    public List<List<OrderResponse>> getApplyOrders(Pageable pageable) {
        return orderAdminService.getApplyOrders(pageable);
    }

    @GetMapping("/orders/{orderCode}")
    public OrderDetailResponse getOrderDetail(@PathVariable("orderCode") String code) {
        return orderAdminService.getOrderDetailByOrderNumber(code);
    }

    @GetMapping("/account/{accountId}/orders")
    public List<OrderResponse> getAccountOrders(@PathVariable("accountId") String accountId) {
        return orderAdminService.getOrdersByAccountId(accountId);
    }

    @PatchMapping("/orders/{orderCode}/status")
    public String updateOrderStatus(@PathVariable("orderCode") String code, @Valid @RequestBody OrderStatusUpdateRequest request) {
        orderAdminService.changeOrderStatus(code, request.getUpdatedOrderStatus());
        return "Updated order status";
    }

    @PatchMapping("/orders/status")
    public String updateOrdersStatus(@Valid @RequestBody OrderStatusUpdateRequest request) {
        orderAdminService.changeOrdersStatus(request.getOrderCodes(), request.getUpdatedOrderStatus());
        return "Updated orders status";
    }

    @GetMapping("/orders/cancel_request")
    public List<OrderResponse> getCancelRequestOrders(Pageable pageable) {
        return orderAdminService.getCancelRequestOrders(pageable);
    }
}
