package know_wave.comma.arduino.order.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.notification.alarm.util.ExceptionMessageSource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

// TODO : 엔티티 상태 변경 메서드에서 예외 처리를 할 지(어떤 예외?), 서비스에서 예외 처리를 할 지 고민
@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Column(updatable = false, nullable = false)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id")
    private Deposit deposit;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private Subject subject;

    public static String generateOrderNumber() {
        return UUID.randomUUID().toString().substring(0, 12);
    }

    public static Order create(String orderNumber, Account account, Deposit deposit, OrderStatus orderStatus, Subject subject) {
        Order order = new Order(null, orderNumber, account, deposit, orderStatus, subject);
        deposit.setOrder(order);
        return order;
    }

    public boolean isAbleToOrder() {
        return orderStatus == OrderStatus.DEPOSIT_PAYMENT_REQUIRED
                && deposit.getDepositStatus() != DepositStatus.REFUND
                || deposit.getDepositStatus() != DepositStatus.COLLECT;
    }

    public boolean isAbleToFreeCancel() {
        return orderStatus == OrderStatus.ORDERED;
    }

    public boolean isDepositSubmitted() {
        return deposit.getDepositStatus() == DepositStatus.PAID;
    }

    private void updateStatus(DepositStatus depositStatus, OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        deposit.setDepositStatus(depositStatus);
    }

    private void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void cancelOrder() {
        if (orderStatus == OrderStatus.ORDERED) {
            updateStatus(DepositStatus.REFUND, OrderStatus.CANCEL);
        } else if (orderStatus == OrderStatus.PREPARING || orderStatus == OrderStatus.BE_READY) {
            updateStatus(DepositStatus.COLLECT, OrderStatus.CANCEL);
        } else {
            throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_CANCEL_ORDER);
        }
    }

    public void rejectOrder() {
        if (orderStatus == OrderStatus.ORDERED || orderStatus == OrderStatus.PREPARING) {
            updateStatus(DepositStatus.REFUND, OrderStatus.FAILURE_CAUSE_REJECTED);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_REJECT_ORDER);
    }

    // 실패 원인에 따라 보증금 환불 또는 회수 처리
    public void refundFailOrder(OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.FAILURE_CAUSE_ARDUINO_STATUS) {
            updateStatus(DepositStatus.REFUND, OrderStatus.FAILURE_CAUSE_ARDUINO_STATUS);
            return;
        } else if (orderStatus == OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK) {
            updateStatus(DepositStatus.REFUND, OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_FAIL_ORDER);
    }

    public void notRefundFailOrder(OrderStatus orderStatus) {
        if (orderStatus == OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE) {
            updateOrderStatus(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE);
            return;
        } else if (orderStatus == OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL) {
            updateOrderStatus(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_FAIL_ORDER);
    }

    public void ordered() {
        if (orderStatus == OrderStatus.DEPOSIT_PAYMENT_REQUIRED) {
            updateStatus(DepositStatus.PAID, OrderStatus.ORDERED);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_ORDER);
    }

    public void prepareOrder() {
        if (orderStatus == OrderStatus.ORDERED) {
            updateOrderStatus(OrderStatus.PREPARING);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_PREPARE_ORDER);
    }

    public void beReadyOrder() {
        if (orderStatus == OrderStatus.PREPARING) {
            updateOrderStatus(OrderStatus.BE_READY);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_BE_READY_ORDER);
    }

    public void receiveOrder() {
        if (orderStatus == OrderStatus.BE_READY) {
            updateOrderStatus(OrderStatus.RECEIVE);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_RECEIVE_ORDER);
    }
}
