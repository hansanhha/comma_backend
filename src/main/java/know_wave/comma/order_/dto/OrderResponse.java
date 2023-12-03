package know_wave.comma.order_.dto;

import know_wave.comma.order_.entity.OrderInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderResponse {

    public static List<List<OrderResponse>> ofGroupList(Map<String, List<OrderResponse>> groupedOrderResponses) {

        List<List<OrderResponse>> result = new ArrayList<>();

        groupedOrderResponses.keySet()
                .forEach(key -> result.add(groupedOrderResponses.get(key)));

        return result;
    }

    public static List<OrderResponse> ofList(List<OrderInfo> orderInfoList) {
        return orderInfoList.stream()
                .map(OrderResponse::of)
                .toList();
    }

    public static OrderResponse of(OrderInfo orderInfo) {
        return new OrderResponse(
                orderInfo.getAccount().getId(),
                orderInfo.getOrderNumber(),
                orderInfo.getStatus().getValue(),
                orderInfo.getCreatedDate(),
                orderInfo.getSubject(),
                orderInfo.getCancellationReason());
    }

    public static Map<String, List<OrderResponse>> groupingOrderByAccountId(List<OrderResponse> orderResponseList) {
        return orderResponseList.stream()
                .collect(Collectors.groupingBy(OrderResponse::getAccountId));
    }

    public static Map<String, List<OrderResponse>> groupingOrderByOrderCode(List<OrderResponse> orderResponses) {
        return orderResponses.stream().
                collect(Collectors.groupingBy(OrderResponse::getOrderCode));
    }
    private OrderResponse(String accountId, String orderCode, String orderStatus, LocalDateTime orderDate, String subject, String cancelReason) {
        this.accountId = accountId;
        this.orderCode = orderCode;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.subject = subject;
        this.cancelReason = cancelReason;
    }
    private final String accountId;
    private final String orderCode;
    private final String orderStatus;
    private final LocalDateTime orderDate;
    private final String subject;

    private final String cancelReason;

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
}
