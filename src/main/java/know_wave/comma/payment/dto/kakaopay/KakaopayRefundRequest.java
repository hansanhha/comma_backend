package know_wave.comma.payment.dto.kakaopay;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaopayRefundRequest {

    public static KakaopayRefundRequest of(String cid, String tid, int cancelAmount, int cancelTaxFreeAmount) {
        return new KakaopayRefundRequest(
                cid,
                tid,
                cancelAmount,
                cancelTaxFreeAmount
        );
    }

    private final String cid;
    private final String tid;
    private final int cancelAmount;
    private final int cancelTaxFreeAmount;
}
