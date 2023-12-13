package know_wave.comma.payment.dto.kakaopay;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaopayReadyResponse {

    private String tid;
    private String next_redirect_pc_url;
    private String next_redirect_mobile_url;
}
