package know_wave.comma.arduino.notification.dto;

import know_wave.comma.arduino.order.entity.DepositStatus;
import know_wave.comma.arduino.order.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderNotificationRequest {

    public static OrderNotificationRequest to(OrderStatus orderStatus, DepositStatus depositStatus, String orderNumber, String accountId) {
        return new OrderNotificationRequest(orderStatus, depositStatus, orderNumber, accountId);
    }

    private final OrderStatus orderStatus;
    private final DepositStatus depositStatus;
    private final String orderNumber;
    private final String accountId;

}
