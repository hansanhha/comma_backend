package know_wave.comma.arduino.order.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderDetailResponse {

    public static AdminOrderDetailResponse to(Order order, List<OrderDetail> orderDetails) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = order.getCreatedDate().format(formatter);

        return new AdminOrderDetailResponse(
                orderDetails.stream().map(OrderDetailResponse::of).toList(),
                order.getOrderNumber(),
                order.getAccount().getId(),
                order.getSubject().getSubjectName(),
                order.getOrderStatus().getStatus(),
                order.isDepositSubmitted(),
                order.getDeposit().getAmount(),
                formattedDate);
    }

    @JsonProperty("order_details")
    private final List<OrderDetailResponse> orderDetails;

    @JsonProperty("order_number")
    private final String orderNumber;

    @JsonProperty("account_id")
    private final String accountId;

    @JsonProperty("subject")
    private final String subject;

    @JsonProperty("order_status")
    private final String orderStatus;

    @JsonProperty("is_deposit_submitted")
    private final Boolean isDepositSubmitted;

    @JsonProperty("deposit_amount")
    private final int depositAmount;

    @JsonProperty("order_date")
    private final String orderDate;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderDetailResponse {

        public static OrderDetailResponse of(OrderDetail orderDetail) {
            return new OrderDetailResponse(
                    orderDetail.getArduino().getId(),
                    orderDetail.getArduino().getName(),
                    orderDetail.getOrderArduinoCount());
        }

        @JsonProperty("arduino_id")
        private final Long arduinoId;

        @JsonProperty("arduino_name")
        private final String arduinoName;

        @JsonProperty("ordered_quantity")
        private final int orderedQuantity;
    }
}
