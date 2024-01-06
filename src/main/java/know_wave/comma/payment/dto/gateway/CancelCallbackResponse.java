package know_wave.comma.payment.dto.gateway;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CancelCallbackResponse {

    public static CancelCallbackResponse create(Map<String, String> cancelCallbackResult) {
        return new CancelCallbackResponse(cancelCallbackResult);
    }

    /*
     * @ param cancelCallbackResult 결제 실패 처리 결과
     */
    private final Map<String, String> cancelCallbackResult;
}
