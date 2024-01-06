package know_wave.comma.payment.dto.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentClientApproveResponse {

    public static PaymentClientApproveResponse create(String tid, String cid, String merchantId, String payerId, int amount, int quantity, String itemName, LocalDateTime paymentReadyDate, LocalDateTime paymentApproveDate) {
        return new PaymentClientApproveResponse(tid, cid, merchantId, payerId, amount, quantity, itemName, paymentReadyDate, paymentApproveDate);
    }

    private final String tid;
    private final String cid;
    private final String merchantId;
    private final String payerId;
    private final int amount;
    private final int quantity;
    private final String itemName;
    private final LocalDateTime paymentReadyDate;
    private final LocalDateTime paymentApproveDate;

}
