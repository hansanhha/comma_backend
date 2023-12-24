package know_wave.comma.arduino.order.dto;

import know_wave.comma.arduino.order.entity.DepositStatus;
import know_wave.comma.arduino.order.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCallbackResponse {

    public static OrderCallbackResponse to(OrderStatus orderStatus, DepositStatus depositStatus) {
        return new OrderCallbackResponse(orderStatus, depositStatus);
    }

    private final OrderStatus orderStatus;
    private final DepositStatus depositStatus;
}
