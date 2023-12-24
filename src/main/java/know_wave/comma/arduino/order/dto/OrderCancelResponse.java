package know_wave.comma.arduino.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderCancelResponse {

    public static OrderCancelResponse to(String orderNumber, LocalDateTime cancelDate, String orderStatus, String depositStatus, int depositAmount) {
        return new OrderCancelResponse(orderNumber, cancelDate, orderStatus, depositStatus, depositAmount);
    }

    private final String orderNumber;
    private final LocalDateTime cancelDate;
    private final String orderStatus;
    private final String depositStatus;
    private final int depositAmount;

}
