package know_wave.comma.arduino.order.admin.dto;

import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderDetailResponse {

    public static AdminOrderDetailResponse to(Order order, List<OrderDetail> orderDetails) {
        return new AdminOrderDetailResponse(
                orderDetails.stream().map(OrderDetailResponse::of).toList(),
                order.getOrderNumber(),
                order.getAccount().getId(),
                order.getSubject().getSubjectName(),
                order.getOrderStatus().getStatus(),
                order.isDepositSubmitted(),
                order.getDeposit().getAmount(),
                order.getCreatedDate());
    }

    private final List<OrderDetailResponse> orderDetails;
    private final String orderNumber;
    private final String accountId;
    private final String subject;
    private final String orderStatus;
    private final boolean isDepositSubmitted;
    private final int depositAmount;
    private final LocalDateTime orderDate;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderDetailResponse {

        public static OrderDetailResponse of(OrderDetail orderDetail) {
            return new OrderDetailResponse(
                    orderDetail.getArduino().getId(),
                    orderDetail.getArduino().getName(),
                    orderDetail.getOrderArduinoCount());
        }

        private final Long arduinoId;
        private final String arduinoName;
        private final int orderedQuantity;
    }
}
