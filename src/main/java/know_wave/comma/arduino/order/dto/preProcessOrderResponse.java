package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class preProcessOrderResponse {

    @JsonProperty("order_number")
    private final String orderNumber;

    @JsonProperty("mobile_redirect_url")
    private final String mobileRedirectUrl;

    @JsonProperty("pc_redirect_url")
    private final String pcRedirectUrl;

    public static preProcessOrderResponse create(String orderNumber, String mobileUrl, String pcUrl) {
        return new preProcessOrderResponse(orderNumber, mobileUrl, pcUrl);
    }
}
