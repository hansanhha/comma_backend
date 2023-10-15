package know_wave.comma.payment.dto.kakao;

import know_wave.comma.payment.entity.Deposit;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoPayApproveRequest {

    private final MultiValueMap<String, Object> value = new LinkedMultiValueMap<>();

    public static KakaoPayApproveRequest of(String cid, Deposit deposit, String tempOrderNumber, String paymentToken) {
        return new KakaoPayApproveRequest(
                cid,
                deposit.getPaymentTransactionId(),
                tempOrderNumber,
                deposit.getAccount().getId(),
                paymentToken
        );
    }

    private KakaoPayApproveRequest(String cid, String transactionId, String tempOrderNumber, String accountId, String paymentToken) {
        value.add("cid", cid);
        value.add("tid", transactionId);
        value.add("partner_order_id", tempOrderNumber);
        value.add("partner_user_id", accountId);
        value.add("pg_token", paymentToken);
    }
}
