package know_wave.comma.payment.dto.kakaopay;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaopayApproveRequest {

    public static KakaopayApproveRequest create(String cid, String tid, String paymentId, String accountId, String pgToken) {

        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.put("cid", Collections.singletonList(cid));
        data.put("tid", Collections.singletonList(tid));
        data.put("partner_order_id", Collections.singletonList(paymentId));
        data.put("partner_user_id", Collections.singletonList(accountId));
        data.put("pg_token", Collections.singletonList(pgToken));

        return new KakaopayApproveRequest(data);
    }

    private final LinkedMultiValueMap<String, String> body;
}
