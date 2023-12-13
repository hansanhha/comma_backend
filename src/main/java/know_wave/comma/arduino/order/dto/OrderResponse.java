package know_wave.comma.arduino.order.dto;

import know_wave.comma.arduino.order.entity.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {

    public static OrderResponse of(Order order) {
        return new OrderResponse(order.getOrderNumber(), order.getCreatedDate(), order.getOrderStatus().getStatus(), order.getDeposit().getDepositStatus().getStatus(), order.getDeposit().getAmount());
    }

    private final String orderNumber;
    private final LocalDateTime orderDate;
    private final String orderStatus;
    private final String depositStatus;
    private final int depositAmount;

}
