package know_wave.comma.arduino.order.admin.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.order.admin.dto.AdminOrderDetailResponse;
import know_wave.comma.arduino.order.admin.dto.AdminOrderPageResponse;
import know_wave.comma.arduino.order.admin.dto.AdminOrderStatusResponse;
import know_wave.comma.arduino.order.admin.dto.OrderStatusUpdateRequest;
import know_wave.comma.arduino.order.admin.service.OrderAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/arduino/orders")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderAdminService orderAdminService;

    private static final String MESSAGE = "msg";
    private static final String DATA = "body";


    @GetMapping
    public Map<String, Object> getOrders(Pageable pageable) {
        AdminOrderPageResponse orderPage = orderAdminService.getAllOrderPage(pageable);

        return Map.of(MESSAGE, "success", DATA, orderPage);
    }

    @GetMapping("/ordered")
    public Map<String, Object> getOrderedOrders(Pageable pageable) {
        AdminOrderPageResponse orderedPage = orderAdminService.getOrderedPage(pageable);

        return Map.of(MESSAGE, "success", DATA, orderedPage);
    }

    @GetMapping("/{orderNumber}")
    public Map<String, Object> getOrderDetail(@PathVariable String orderNumber) {
        AdminOrderDetailResponse orderDetail = orderAdminService.getOrderDetail(orderNumber);

        return Map.of(MESSAGE, "success", DATA, orderDetail);
    }

    @PatchMapping("/{orderNumber}")
    public Map<String, Object> changeOrderStatus(@PathVariable String orderNumber,
                                                 @Valid @RequestBody OrderStatusUpdateRequest updateRequest) {
        AdminOrderStatusResponse adminOrderStatusResponse = orderAdminService.updateOrderStatus(orderNumber, updateRequest);

        return Map.of(MESSAGE, "success", DATA, adminOrderStatusResponse);
    }

}
