package know_wave.comma.arduino.order.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class OrderStatusUpdateRequest {

    @NotEmpty(message = "주문 상태를 입력해주세요.")
    @JsonProperty("update_order_status")
    private String orderStatus;
}
