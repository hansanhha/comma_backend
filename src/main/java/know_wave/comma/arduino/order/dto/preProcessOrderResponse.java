package know_wave.comma.arduino.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class preProcessOrderResponse {

    private final String orderNumber;
    private final String mobileRedirectUrl;
    private final String pcRedirectUrl;

    public static preProcessOrderResponse to(String orderNumber, String mobileUrl, String pcUrl) {
        return new preProcessOrderResponse(orderNumber, mobileUrl, pcUrl);
    }
}
