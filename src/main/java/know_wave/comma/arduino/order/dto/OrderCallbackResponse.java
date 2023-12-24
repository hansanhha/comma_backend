package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.order.entity.DepositStatus;
import know_wave.comma.arduino.order.entity.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCallbackResponse {

    public static OrderCallbackResponse create(OrderStatus orderStatus, DepositStatus depositStatus) {
        return new OrderCallbackResponse(orderStatus, depositStatus);
    }

    @JsonProperty("order_status")
    private final OrderStatus orderStatus;

    @JsonProperty("deposit_status")
    private final DepositStatus depositStatus;
}
