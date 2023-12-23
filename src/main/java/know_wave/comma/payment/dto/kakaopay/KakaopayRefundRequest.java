package know_wave.comma.payment.dto.kakaopay;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaopayRefundRequest {

    public static KakaopayRefundRequest create(String cid, String tid, int cancelAmount, int cancelTaxFreeAmount) {
        LinkedMultiValueMap<String, String> data = new LinkedMultiValueMap<>();

        data.put("cid", Collections.singletonList(cid));
        data.put("tid", Collections.singletonList(tid));
        data.put("cancel_amount", Collections.singletonList(String.valueOf(cancelAmount)));
        data.put("cancel_tax_free_amount", Collections.singletonList(String.valueOf(cancelTaxFreeAmount)));

        return new KakaopayRefundRequest(data);
    }

    private final LinkedMultiValueMap<String, String> body;
}
