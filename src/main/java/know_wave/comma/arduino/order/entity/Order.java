package know_wave.comma.arduino.order.entity;

import jakarta.persistence.*;
import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoStockStatus;
import know_wave.comma.common.entity.BaseTimeEntity;
import know_wave.comma.common.entity.ExceptionMessageSource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Entity(name = "arduino_order")
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

    public static OrderStatus validate(Arduino arduino, int orderQuantity) {
        ArduinoStockStatus stockStatus = arduino.getStockStatus();

        if (stockStatus == ArduinoStockStatus.NONE || stockStatus == ArduinoStockStatus.UP_COMMING) {
            return OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS;
        } else if (orderQuantity > arduino.getCount()) {
            return OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK;
        } else
            return OrderStatus.VALID;
    }

    public static OrderStatus validateList(List<OrderDetail> details, int maxQuantity) {
        return details.stream()
                .map(detail -> validate(detail.getArduino(), detail.getOrderArduinoCount()))
                .filter(orderStatus -> orderStatus != OrderStatus.VALID)
                .findFirst()
                .orElse(OrderStatus.VALID);
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

    public void freeCancelOrder() {
        if (isAbleToFreeCancel()) {
            if (orderStatus == OrderStatus.ORDERED) {
                updateStatus(DepositStatus.RETURN, OrderStatus.FREE_CANCEL);
                return;
            }
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_CANCEL_ORDER);
    }

    public void cancelOrder() {
        if (orderStatus == OrderStatus.PREPARING || orderStatus == OrderStatus.BE_READY) {
            updateStatus(DepositStatus.RECLAIMED, OrderStatus.CANCEL);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_CANCEL_ORDER);
    }

    public void rejectOrder() {
        if (orderStatus == OrderStatus.ORDERED || orderStatus == OrderStatus.PREPARING) {
            updateStatus(DepositStatus.RETURN, OrderStatus.FAILURE_CAUSE_REJECTED);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_REJECT_ORDER);
    }

    public void handleDepositReturn() {
        if (deposit.getDepositStatus() == DepositStatus.RETURN_SCHEDULED) {
            deposit.setDepositStatus(DepositStatus.RETURN);
            return;
        }
    }
    
    // 보증금 결제 후 주문 실패 시 보증금 반환 예정 처리
    public void handleFailureDuringOrderProcessing(OrderStatus cause) {
        if (cause == OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS) {
            updateStatus(DepositStatus.RETURN_SCHEDULED, OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS);
            return;
        } else if (cause == OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK) {
            updateStatus(DepositStatus.RETURN_SCHEDULED, OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_FAIL_ORDER);
    }

    public void handleFailureCauseDepositPayment(OrderStatus cause) {
        if (cause == OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE) {
            updateOrderStatus(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE);
            return;
        } else if (cause == OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL) {
            updateOrderStatus(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL);
            return;
        }

        throw new IllegalStateException(ExceptionMessageSource.UNABLE_TO_FAIL_ORDER);
    }

    public void order(List<OrderDetail> orderDetails) {
        if (orderStatus == OrderStatus.DEPOSIT_PAYMENT_REQUIRED) {
            updateStatus(DepositStatus.PAID, OrderStatus.ORDERED);

            orderDetails.forEach(orderDetail ->
                    orderDetail.getArduino().decreaseStock(orderDetail.getOrderArduinoCount()));

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
