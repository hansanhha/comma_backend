package know_wave.comma.order_.dto;

import know_wave.comma.order_.entity.Subject;
import know_wave.comma.payment_.entity.PaymentType;
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
