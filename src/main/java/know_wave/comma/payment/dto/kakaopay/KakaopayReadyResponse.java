package know_wave.comma.payment.dto.kakaopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaopayReadyResponse {

    @JsonProperty("tid")
    private String tid;
    @JsonProperty("next_redirect_app_url")
    private String next_redirect_pc_url;
    @JsonProperty("next_redirect_pc_url")
    private String next_redirect_mobile_url;
}
