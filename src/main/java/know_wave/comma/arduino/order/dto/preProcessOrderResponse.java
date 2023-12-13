package know_wave.comma.arduino.order.dto;

import know_wave.comma.payment.entity.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class preProcessOrderResponse {

    private final String orderNumber;
    private final String mobileRedirectUrl;
    private final String pcRedirectUrl;

    public static preProcessOrderResponse of(String orderNumber, String mobileUrl, String pcUrl) {
        return new preProcessOrderResponse(orderNumber, mobileUrl, pcUrl);
    }
}
