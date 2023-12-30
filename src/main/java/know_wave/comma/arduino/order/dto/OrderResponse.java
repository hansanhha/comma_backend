package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.order.entity.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {

    public static OrderResponse to(Order order) {
        return new OrderResponse(order.getOrderNumber(), order.getCreatedDate(), order.getOrderStatus().getStatus(), order.getDeposit().getDepositStatus().getStatus(), order.getDeposit().getAmount());
    }

    @JsonProperty("order_number")
    private final String orderNumber;

    @JsonProperty("order_date")
    private final LocalDateTime orderDate;

    @JsonProperty("order_status")
    private final String orderStatus;

    @JsonProperty("deposit_status")
    private final String depositStatus;

    @JsonProperty("deposit_amount")
    private final int depositAmount;

}
