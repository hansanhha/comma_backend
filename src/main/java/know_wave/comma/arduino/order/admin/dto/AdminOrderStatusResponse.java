package know_wave.comma.arduino.order.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderStatusResponse {

    public static AdminOrderStatusResponse create(String orderNumber, String beforeOrderStatus, String afterOrderStatus, String depositStatus) {
        return new AdminOrderStatusResponse(orderNumber, beforeOrderStatus, afterOrderStatus, depositStatus);
    }

    @JsonProperty("order_number")
    private final String orderNumber;

    @JsonProperty("before_order_status")
    private final String beforeOrderStatus;

    @JsonProperty("after_order_status")
    private final String afterOrderStatus;

    @JsonProperty("deposit_status")
    private final String depositStatus;

}
