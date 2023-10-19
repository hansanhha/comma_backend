package com.know_wave.comma.comma_backend.order.dto;

import com.know_wave.comma.comma_backend.order.entity.OrderStatus;
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
