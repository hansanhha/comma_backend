package know_wave.comma.payment.entity;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class PaymentCbUrl {

    private final Map<String, String> map = new HashMap<>();

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
    private String cid;

    @Value("${payment.success-callback-url}")
    private String paymentSuccessCbUrl;

    @Value("${payment.fail-callback-url}")
    private String paymentFailCbUrl;

    @Value("${payment.cancel-callback-url}")
    private String paymentCancelCbUrl;

    @PostConstruct
    public void init() {
        map.put(FAIL_CB_URL_KEY, paymentFailCbUrl);
        map.put(CANCEL_CB_URL_KEY, paymentCancelCbUrl);
        map.put(SUCCESS_CB_URL_KEY, paymentSuccessCbUrl);
        map.put(CID_KEY, cid);
    }
}
