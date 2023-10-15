package know_wave.comma.payment.dto.kakao;

import know_wave.comma.payment.entity.Deposit;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KaKaoPayCancelRequest {

    private final MultiValueMap<String, Object> value = new LinkedMultiValueMap<>();

    public static KaKaoPayCancelRequest of(String cid, Deposit deposit) {
        return new KaKaoPayCancelRequest(
                cid,
                deposit.getPaymentTransactionId(),
                deposit.getTotalAmount()
        );
    }

    private KaKaoPayCancelRequest(String cid, String transactionId, int amount) {
        value.add("cid", cid);
        value.add("tid", transactionId);
        value.add("cancel_amount", amount);
        value.add("cancel_tax_free_amount", amount);
    }
}
