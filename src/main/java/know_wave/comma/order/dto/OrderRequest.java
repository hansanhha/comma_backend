package know_wave.comma.order.dto;

import know_wave.comma.order.entity.Subject;
import know_wave.comma.payment.entity.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRequest {

    @NotNull(message = "{Required}")
    private Subject subject;

    @NotNull(message = "{Required}")
    private PaymentType paymentType;

    private String sseId;
}
