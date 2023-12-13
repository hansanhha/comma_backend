package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequest {

    @JsonProperty("subject")
    private final String subject;

    @JsonProperty("paymentType")
    private final String paymentType;

    @JsonProperty("idempotency_key")
    private final String idempotencyKey;
}
