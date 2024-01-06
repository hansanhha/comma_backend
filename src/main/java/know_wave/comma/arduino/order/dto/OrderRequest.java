package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class OrderRequest {

    public static OrderRequest create(String subject, String paymentType) {
        return new OrderRequest(subject, paymentType);
    }

    @JsonCreator
    private OrderRequest(String subject, String paymentType) {
        this.subject = subject;
        this.paymentType = paymentType;
    }

    @NotEmpty(message = "과목을 입력해주세요")
    @JsonProperty("subject")
    private final String subject;

    @NotEmpty(message = "결제 수단을 선택해주세요")
    @JsonProperty("payment_type")
    private final String paymentType;

}
