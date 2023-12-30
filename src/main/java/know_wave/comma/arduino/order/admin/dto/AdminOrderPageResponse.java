package know_wave.comma.arduino.order.admin.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.order.entity.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderPageResponse {

    public static AdminOrderPageResponse to(Page<Order> orderPage) {
        return new AdminOrderPageResponse(
                orderPage.stream().map(AdminOrderResponse::to).toList(),
                orderPage.isFirst(),
                orderPage.isLast(),
                orderPage.hasNext(),
                orderPage.getSize());
    }

    @JsonProperty("orders")
    private final List<AdminOrderResponse> orders;

    @JsonProperty("is_first")
    private final Boolean isFirst;

    @JsonProperty("is_last")
    private final Boolean isLast;

    @JsonProperty("has_next")
    private final Boolean hasNext;

    @JsonProperty("size")
    private final int size;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class AdminOrderResponse{
        public static AdminOrderResponse to(Order order) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = order.getCreatedDate().format(formatter);

            return new AdminOrderResponse(
                    order.getOrderNumber(),
                    order.getAccount().getId(),
                    order.getSubject().getSubjectName(),
                    order.getOrderStatus().getStatus(),
                    order.isDepositSubmitted(),
                    order.getDeposit().getAmount(),
                    formattedDate);
        }

        @JsonProperty("order_number")
        private final String orderNumber;

        @JsonProperty("account_id")
        private final String accountId;

        @JsonProperty("subject")
        private final String subject;

        @JsonProperty("order_status")
        private final String orderStatus;

        @JsonProperty("is_deposit_submitted")
        private final Boolean isDepositSubmitted;

        @JsonProperty("deposit_amount")
        private final int depositAmount;

        @JsonProperty("order_date")
        private final String orderDate;
    }

}
