package know_wave.comma.order_.dto;

import know_wave.comma.arduino_.entity.Arduino;
import know_wave.comma.arduino_.entity.ArduinoCategory;
import know_wave.comma.order_.entity.OrderInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderDetailResponse {

    public static OrderDetailResponse of(OrderInfo orderInfo, Map<Arduino, List<ArduinoCategory>> categoryMap) {
        return new OrderDetailResponse(
                orderInfo.getAccount().getId(),
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

    private OrderDetailResponse(String accountId, LocalDateTime orderDate, String orderStatus, String orderCode, String subject, String cancelReason, List<OrderArduino> orderList) {
        this.accountId = accountId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderCode = orderCode;
        this.subject = subject;
        this.cancelReason = cancelReason;
        this.orderList = orderList;
    }

    private final String accountId;
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
