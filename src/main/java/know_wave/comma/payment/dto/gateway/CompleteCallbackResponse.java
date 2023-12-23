package know_wave.comma.payment.dto.gateway;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CompleteCallbackResponse {

    public static CompleteCallbackResponse create(Map<String, String> completeCallbackResult) {
        return new CompleteCallbackResponse(completeCallbackResult);
    }

    /*
     * @ param completeResult 결제 완료 처리 결과
     */
    private final Map<String,String> completeCallbackResult;

}
