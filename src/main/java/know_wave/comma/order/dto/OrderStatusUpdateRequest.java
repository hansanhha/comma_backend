package know_wave.comma.order.dto;

import know_wave.comma.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderStatusUpdateRequest {

    @NotNull(message = "{Required}")
    private OrderStatus updatedOrderStatus;
    private List<String> orderCodes;

    public OrderStatus getUpdatedOrderStatus() {
        return updatedOrderStatus;
    }

    public void setUpdatedOrderStatus(OrderStatus updatedOrderStatus) {
        this.updatedOrderStatus = updatedOrderStatus;
    }

    public List<String> getOrderCodes() {
        return orderCodes;
    }

    public void setOrderCodes(List<String> orderCodes) {
        this.orderCodes = orderCodes;
    }
}
