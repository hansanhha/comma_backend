package know_wave.comma.payment.dto.gateway;

import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentStatus;
import know_wave.comma.payment.entity.PaymentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayRefundResponse {

    public static PaymentGatewayRefundResponse of(String paymentRequestId, String accountId, PaymentStatus paymentStatus, PaymentFeature paymentFeature, PaymentType paymentType, int amount, int cancelAmount, int quantity, String itemName, LocalDateTime paymentReadyDate, LocalDateTime paymentApproveDate, LocalDateTime paymentCancelDate) {
        return new PaymentGatewayRefundResponse(paymentRequestId, accountId, paymentStatus, paymentFeature, paymentType, amount, cancelAmount, quantity, itemName, paymentReadyDate, paymentApproveDate, paymentCancelDate);
    }

    private final String paymentRequestId;
    private final String accountId;
    private final PaymentStatus paymentStatus;
    private final PaymentFeature paymentFeature;
    private final PaymentType paymentType;
    private final int amount;
    private final int cancelAmount;
    private final int quantity;
    private final String itemName;
    private final LocalDateTime paymentReadyDate;
    private final LocalDateTime paymentApproveDate;
    private final LocalDateTime paymentCancelDate;
}
