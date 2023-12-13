package know_wave.comma.payment.dto.kakaopay;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaopayApproveRequest {

    public static KakaopayApproveRequest of(String cid, String tid, String paymentId, String accountId, String pgToken) {
        return new KakaopayApproveRequest(
                cid,
                tid,
                paymentId,
                accountId,
                pgToken
        );
    }

    private final String cid;
    private final String tid;
    private final String partnerOrderId;
    private final String partnerUserId;
    private final String pgToken;

}
