package know_wave.comma.payment.dto;

import know_wave.comma.payment.entity.PaymentType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRefundRequest {

    private Long paymentId;
    private PaymentType paymentType;
}
