package com.know_wave.comma.comma_backend.arduino.dto.order;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.ArduinoCategory;
import com.know_wave.comma.comma_backend.arduino.entity.OrderInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderDetailResponse {

    public static OrderDetailResponse of(OrderInfo orderInfo, Map<Arduino, List<ArduinoCategory>> categoryMap) {
        return new OrderDetailResponse(
                orderInfo.getId(),
                orderInfo.getDescription(),
                orderInfo.getCreatedDate(),
                orderInfo.getStatus().getValue(),
                orderInfo.getOrderNumber(),
                orderInfo.getSubject(),
                orderInfo.getCancellationReason(),
                orderInfo.getOrders().stream()
                        .map(order -> OrderArduino.of(order, categoryMap))
                        .toList()
        );
    }

    private OrderDetailResponse(String accountId, String orderDescription, LocalDateTime orderDate, String orderStatus, String orderCode, String subject, String cancelReason, List<OrderArduino> orderList) {
        this.accountId = accountId;
        this.orderDescription = orderDescription;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderCode = orderCode;
        this.subject = subject;
        this.cancelReason = cancelReason;
        this.orderList = orderList;
    }

    private final String accountId;
    private final String orderDescription;
    private final LocalDateTime orderDate;
    private final String orderStatus;
    private final String orderCode;
    private final String subject;
    private final String cancelReason;
    private final List<OrderArduino> orderList;

    public String getCancelReason() {
        return cancelReason;
    }
    public String getAccountId() {
        return accountId;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getSubject() {
        return subject;
    }

    public List<OrderArduino> getOrderList() {
        return orderList;
    }
}
