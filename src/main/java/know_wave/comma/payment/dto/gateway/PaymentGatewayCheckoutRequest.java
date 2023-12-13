package know_wave.comma.payment.dto.gateway;

import know_wave.comma.account.entity.Account;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class PaymentGatewayCheckoutRequest {

    public static PaymentGatewayCheckoutRequest of(String orderNumber, Account account, PaymentType paymentType, PaymentFeature feature, int amount, int quantity) {
        return new PaymentGatewayCheckoutRequest(orderNumber, account, paymentType, feature, amount, quantity);
    }

    private final String orderNumber;
    private final Account account;
    private final PaymentType paymentType;
    private final PaymentFeature paymentFeature;
    private final int amount;
    private final int quantity;
}
