package know_wave.comma.payment.entity;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PaymentCbUrl {

    private final Map<PaymentType, Map<String,String>> map = new HashMap<>();

    public Map<String, String> getMap(PaymentType type) {
        return map.get(type);
    }

    public static final String CID_KEY = "cid";
    public static final String SUCCESS_CB_URL_KEY = "successUrl";
    public static final String CANCEL_CB_URL_KEY = "cancelUrl";
    public static final String FAIL_CB_URL_KEY = "failUrl";
    public static final String READY_REQUEST_URL_KEY = "readyRequestUrl";
    public static final String APPROVE_REQUEST_URL_KEY = "approveRequestUrl";
    public static final String CANCEL_REQUEST_URL_KEY = "cancelRequestUrl";
    public static final String AUTH_HEADER_NAME_KEY = "authorizationHeaderName";
    public static final String AUTH_HEADER_PREFIX_KEY = "authorizationHeaderPrefix";

    @Value("${kakao.api.pay.cid}")
    private String kakaoCid;

    @Value("${payment.success-callback-url}")
    private String kakaoPaySuccessCbUrl;

    @Value("${payment.fail-callback-url}")
    private String kakaoPayFailCbUrl;

    @Value("${payment.cancel-callback-url}")
    private String kakaoPayCancelCbUrl;

    @PostConstruct
    public void init() {
        map.put(PaymentType.KAKAO_PAY, new HashMap<>());
        map.get(PaymentType.KAKAO_PAY).put(CID_KEY, kakaoCid);
        map.get(PaymentType.KAKAO_PAY).put(SUCCESS_CB_URL_KEY, kakaoPaySuccessCbUrl);
        map.get(PaymentType.KAKAO_PAY).put(CANCEL_CB_URL_KEY, kakaoPayCancelCbUrl);
        map.get(PaymentType.KAKAO_PAY).put(FAIL_CB_URL_KEY, kakaoPayFailCbUrl);
    }

}
