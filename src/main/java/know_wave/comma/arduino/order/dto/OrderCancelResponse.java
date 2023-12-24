package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderCancelResponse {

    public static OrderCancelResponse create(String orderNumber, LocalDateTime cancelDate, String orderStatus, String depositStatus, int depositAmount) {
        return new OrderCancelResponse(orderNumber, cancelDate, orderStatus, depositStatus, depositAmount);
    }

    @JsonProperty("order_number")
    private final String orderNumber;

    @JsonProperty("cancel_date")
    private final LocalDateTime cancelDate;

    @JsonProperty("order_status")
    private final String orderStatus;

    @JsonProperty("deposit_status")
    private final String depositStatus;

    @JsonProperty("deposit_amount")
    private final int depositAmount;

}
