package know_wave.comma.payment.dto.client;

import know_wave.comma.payment.entity.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentClientRefundRequest {

    public static PaymentClientRefundRequest of(String tid, int amount, PaymentType paymentType) {
        return new PaymentClientRefundRequest(tid, amount, paymentType);
    }

    private final String tid;
    private final int amount;
    private final PaymentType paymentType;
}
