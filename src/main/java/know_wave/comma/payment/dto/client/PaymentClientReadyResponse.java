package know_wave.comma.payment.dto.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentClientReadyResponse {

    private final String mobileRedirectUrl;
    private final String pcRedirectUrl;
    private final String tid;

    public static PaymentClientReadyResponse of(String mobileUrl, String pcUrl, String tid) {
        return new PaymentClientReadyResponse(mobileUrl, pcUrl, tid);
    }
}
