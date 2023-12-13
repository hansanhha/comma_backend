package know_wave.comma.payment.dto.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentClientRefundResponse {

    public static PaymentClientRefundResponse of(String tid, String cid, String merchantId, String payerId, String paymentStatus, int amount, int cancelAmount, int quantity, String itemName, LocalDateTime paymentReadyDate, LocalDateTime paymentApproveDate, LocalDateTime paymentCancelDate) {
        return new PaymentClientRefundResponse(tid, cid, merchantId, payerId, paymentStatus, amount, cancelAmount, quantity, itemName, paymentReadyDate, paymentApproveDate, paymentCancelDate);
    }

    private final String tid;
    private final String cid;
    private final String merchantId;
    private final String payerId;
    private final String paymentStatus;
    private final int amount;
    private final int cancelAmount;
    private final int quantity;
    private final String itemName;
    private final LocalDateTime paymentReadyDate;
    private final LocalDateTime paymentApproveDate;
    private final LocalDateTime paymentCancelDate;

}
