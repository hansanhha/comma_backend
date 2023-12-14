package know_wave.comma.arduino.order.admin.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderStatusResponse {

    public static AdminOrderStatusResponse to(String orderNumber, String beforeOrderStatus, String afterOrderStatus, String depositStatus) {
        return new AdminOrderStatusResponse(orderNumber, beforeOrderStatus, afterOrderStatus, depositStatus);
    }

    private final String orderNumber;
    private final String beforeOrderStatus;
    private final String afterOrderStatus;
    private final String depositStatus;

}
