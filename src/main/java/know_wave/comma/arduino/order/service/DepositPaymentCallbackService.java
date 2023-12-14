package know_wave.comma.arduino.order.service;

import jakarta.transaction.Transactional;
import know_wave.comma.arduino.order.dto.OrderCallbackResponse;
import know_wave.comma.arduino.order.entity.DepositStatus;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import know_wave.comma.arduino.order.entity.OrderStatus;
import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.notification.base.service.NotificationGateway;
import know_wave.comma.payment.dto.gateway.*;
import know_wave.comma.payment.entity.Payment;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.service.PaymentCallbackService;
import know_wave.comma.payment.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositPaymentCallbackService implements PaymentCallbackService {

    private final PaymentGateway paymentGateway;
    private final NotificationGateway notificationGateway;
    private final OrderService orderService;

    private static final PaymentFeature PAYMENT_FEATURE = PaymentFeature.ARDUINO_DEPOSIT;
    private static final String ORDER_STATUS = "orderStatus";
    private static final String DEPOSIT_STATUS = "depositStatus";

    @Override
    public CompleteCallbackResponse complete(CompleteCallback completeCallback) {
        OrderCallbackResponse orderResponse = order(completeCallback.getOrderNumber());

        Map<String, String> response = Map.of(ORDER_STATUS, orderResponse.getOrderStatus(),
                DEPOSIT_STATUS, orderResponse.getDepositStatus());

        return CompleteCallbackResponse.of(response);
    }

    @Override
    public CancelCallbackResponse cancel(CancelCallback cancelCallback) {
        OrderCallbackResponse orderResponse = failOrder(cancelCallback.getOrderNumber(),
                OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL);

        Map<String, String> response = Map.of(ORDER_STATUS, orderResponse.getOrderStatus(),
                DEPOSIT_STATUS, orderResponse.getDepositStatus());

        return CancelCallbackResponse.of(response);
    }

    @Override
    public FailCallbackResponse fail(FailCallback failCallback) {
        OrderCallbackResponse orderResponse = failOrder(failCallback.getOrderNumber(),
                OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE);

        Map<String, String> response = Map.of(ORDER_STATUS, orderResponse.getOrderStatus(),
                DEPOSIT_STATUS, orderResponse.getDepositStatus());

        return FailCallbackResponse.of(response);
    }

    @Override
    public boolean isSupport(PaymentFeature paymentFeature) {
        return PAYMENT_FEATURE == paymentFeature;
    }

    /* 사용자 결제 성공 시 호출됨
     * 1. 보증금 및 결제 상태 확인
     * 2. 아두이노 부품 상태 및 재고 확인
     * 2.1 재고 부족 시 예외 처리 (보증금 환불, 주문 취소 처리)
     * 3. 보증금 상태 변경
     * 4. 재고 차감
     * 5. 주문 정보 반환
     */
    private OrderCallbackResponse order(String orderNumber) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);
        List<OrderDetail> orderDetails = orderService.findOrderDetails(order);
        Payment payment = order.getDeposit().getPayment();

        if (order.isAbleToOrder()) {
            throw new OrderException(ExceptionMessageSource.UNABLE_TO_ORDER);
        }

        if (!payment.isPaymentComplete()) {
            throw new OrderException(ExceptionMessageSource.UNABLE_TO_ORDER);
        }

        boolean invalidArduino = orderDetails.stream()
                .anyMatch(orderDetail -> !orderDetail.getArduino().isValid(orderDetail.getOrderArduinoCount()));

        if (invalidArduino) {
            // 아두이노 부품 재고 부족 또는 주문 불가 상태일 경우 예외 처리 (보증금 환불, 주문 취소 처리)
            PaymentGatewayRefundResponse refund =
                    paymentGateway.refund(order.getDeposit().getPayment().getPaymentRequestId());

            order.refundFailOrder(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK);


            return OrderCallbackResponse.to(order.getOrderStatus().getStatus(), order.getDeposit().getDepositStatus().getStatus());
        }

        order.ordered();
        orderDetails.forEach(orderDetail ->
                orderDetail.getArduino().decreaseStock(orderDetail.getOrderArduinoCount()));

        return OrderCallbackResponse.to(order.getOrderStatus().getStatus(), order.getDeposit().getDepositStatus().getStatus());
    }

    private OrderCallbackResponse failOrder(String orderNumber, OrderStatus orderStatus) {
        Order order = orderService.findOrderByOrderNumber(orderNumber);

        order.notRefundFailOrder(orderStatus);

        return OrderCallbackResponse.to(DepositStatus.REQUIRED.getStatus(), orderStatus.getStatus());
    }
}
