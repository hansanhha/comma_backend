package know_wave.comma.arduino.order.admin.dto;

import know_wave.comma.arduino.order.entity.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminOrderPageResponse {

    public static AdminOrderPageResponse of(Page<Order> orderPage) {
        return new AdminOrderPageResponse(
                orderPage.stream().map(AdminOrderResponse::of).toList(),
                orderPage.isFirst(),
                orderPage.isLast(),
                orderPage.hasNext(),
                orderPage.getSize());
    }

    private final List<AdminOrderResponse> orders;
    private final boolean isFirst;
    private final boolean isLast;
    private final boolean hasNext;
    private final int size;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class AdminOrderResponse{
        public static AdminOrderResponse of(Order order) {
            return new AdminOrderResponse(
                    order.getOrderNumber(),
                    order.getAccount().getId(),
                    order.getSubject().getSubjectName(),
                    order.getOrderStatus().getStatus(),
                    order.isDepositSubmitted(),
                    order.getDeposit().getAmount(),
                    order.getCreatedDate());
        }

        private final String orderNumber;
        private final String accountId;
        private final String subject;
        private final String orderStatus;
        private final boolean isDepositSubmitted;
        private final int depositAmount;
        private final LocalDateTime orderDate;
    }

}
