package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequest {

    public static OrderRequest create(String subject, String paymentType) {
        return new OrderRequest(subject, paymentType);
    }

    @NotEmpty(message = "과목을 입력해주세요")
    @JsonProperty("subject")
    private final String subject;

    @NotEmpty(message = "결제 수단을 선택해주세요")
    @JsonProperty("payment_type")
    private final String paymentType;

}
