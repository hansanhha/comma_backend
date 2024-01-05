package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayCheckoutResponse {

    private final Payment payment;
    private final String mobileRedirectUrl;
    private final String pcRedirectUrl;
    private final String idempotencyKey;

    public static PaymentGatewayCheckoutResponse create(Payment payment, String mobileUrl, String pcUrl, String paymentRequestId) {
        return new PaymentGatewayCheckoutResponse(payment, mobileUrl, pcUrl, paymentRequestId);
    }
}
