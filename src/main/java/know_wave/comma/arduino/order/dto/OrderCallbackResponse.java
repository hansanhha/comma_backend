package know_wave.comma.arduino.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCallbackResponse {

    public static OrderCallbackResponse of(String orderStatus, String depositStatus) {
        return new OrderCallbackResponse(orderStatus, depositStatus);
    }

    private final String orderStatus;
    private final String depositStatus;
}
