package know_wave.comma.payment.dto.kakaopay;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class KakaopayRefundResponse {

    private String tid;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String status;
    private int amount;
    private int approved_cancel_amount;
    private int quantity;
    private String item_name;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private LocalDateTime canceled_at;
}
