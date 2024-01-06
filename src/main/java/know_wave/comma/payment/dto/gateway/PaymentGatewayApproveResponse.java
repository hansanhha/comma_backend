package know_wave.comma.payment.dto.gateway;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class PaymentGatewayApproveResponse {

    public static PaymentGatewayApproveResponse create(Map<String, String> transactionResult, String paymentRequestId, String orderNumber, String accountId, String paymentStatus, String paymentFeature, String paymentType, int amount, int quantity, LocalDateTime paymentReadyDate, LocalDateTime paymentApproveDate) {
        return new PaymentGatewayApproveResponse(transactionResult, paymentRequestId, orderNumber, accountId, paymentStatus, paymentFeature, paymentType, amount, quantity, paymentReadyDate, paymentApproveDate);
    }

    private final Map<String, String> transactionResult;
    private final String paymentRequestId;
    private final String orderNumber;
    private final String accountId;
    private final String paymentStatus;
    private final String paymentFeature;
    private final String paymentType;
    private final int amount;
    private final int quantity;
    private final LocalDateTime paymentReadyDate;
    private final LocalDateTime paymentApproveDate;
}
