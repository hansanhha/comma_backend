package know_wave.comma.payment.dto.kakao;

import java.time.LocalDateTime;

public record KaKaoPayCancelResponse(
        String aid,
        String tid,
        String cid,
        String status,
        String partner_order_id,
        String partner_user_id,
        String payment_method_type,
        Amount amount,
        ApprovedCancelAmount approved_cancel_amount,
        CanceledAmount canceled_amount,
        CancelAvailableAmount cancel_available_amount,
        String item_name,
        String item_code,
        int quantity,
        LocalDateTime created_at,
        LocalDateTime approved_at,
        LocalDateTime canceled_at,
        String payload
) {


}
