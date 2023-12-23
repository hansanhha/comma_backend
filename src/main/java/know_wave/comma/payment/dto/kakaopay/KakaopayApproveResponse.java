package know_wave.comma.payment.dto.kakaopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class KakaopayApproveResponse {

    @JsonProperty("tid")
    private String tid;
    @JsonProperty("cid")
    private String cid;
    @JsonProperty("partner_order_id")
    private String partner_order_id;
    @JsonProperty("partner_user_id")
    private String partner_user_id;
    @JsonProperty("amount.total")
    private int amount;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("item_name")
    private String item_name;
    @JsonProperty("created_at")
    private LocalDateTime created_at;
    @JsonProperty("approved_at")
    private LocalDateTime approved_at;

}
