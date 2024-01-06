package know_wave.comma.arduino.order.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountCheckService;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.cart.dto.CartValidateStatus;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.cart.exception.CartException;
import know_wave.comma.arduino.cart.service.CartService;
import know_wave.comma.arduino.notification.dto.OrderNotificationRequest;
import know_wave.comma.arduino.notification.service.ArduinoOrderNotification;
import know_wave.comma.arduino.order.dto.*;
import know_wave.comma.arduino.order.entity.*;
import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.arduino.order.repository.DepositRepository;
import know_wave.comma.arduino.order.repository.OrderDetailRepository;
import know_wave.comma.arduino.order.repository.OrderRepository;
import know_wave.comma.account.aop.CheckAccountStatus;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutRequest;
import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutResponse;
import know_wave.comma.payment.dto.gateway.PaymentGatewayRefundResponse;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentType;
import know_wave.comma.payment.exception.PaymentException;
import know_wave.comma.payment.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@CheckAccountStatus
public class OrderCommandService {

    private final CartService cartService;
    private final AccountQueryService accountQueryService;
    private final AccountCheckService accountCheckService;
    private final ArduinoOrderNotification orderNotification;
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DepositRepository depositRepository;
    private static final int DEPOSIT_AMOUNT = 3000;
    private static final int PAYMENT_QUANTITY = 1;
    @Value("${arduino.max-order-quantity}")
    private int ORDER_MAX_QUANTITY;

    {
        ORDER_MAX_QUANTITY = 4;
    }

    /* 0. 멱등성 검증 (컨트롤러에서 처리)
     * 1. 장바구니, 수량 및 재고 상태 검증
     * 2. 주문 및 보증금 정보 생성
     * 3. 카카오페이 결제 요청(결제 준비 단계)
     *
     * sse,websocket(real-time communication)
     * 1. 연결 : 주문 전처리 단계(preProcessOrder())
     * 2. 알림 : 결제 콜백 서비스(PaymentCallbackService)에서 결제 결과에 따른 주문 처리 알림
     * notification
     * 1. 알림 : 결제 콜백 서비스 -> notification 서비스 호출 후 결제 결과에 따른 주문 처리 알림
     */
    public preProcessOrderResponse preProcessOrder(OrderRequest orderRequest) {
        accountCheckService.validateOrderAuthority();

        List<Cart> cartList = cartService.findCartList();

        if (cartList.isEmpty()) {
            throw new CartException(ExceptionMessageSource.EMPTY_BASKET);
        }

        CartValidateStatus cartValidateStatus = Cart.validateList(cartList, ORDER_MAX_QUANTITY);

        if (cartValidateStatus.isOverMaxQuantity()) {
            throw new OrderException(ExceptionMessageSource.OVER_MAX_ARDUINO_QUANTITY);
        } else if (cartValidateStatus.isBadArduinoStatus()) {
            throw new OrderException(ExceptionMessageSource.BAD_ARDUINO_STOCK_STATUS);
        } else if (cartValidateStatus.isNotEnoughStock()) {
            throw new OrderException(ExceptionMessageSource.NOT_ENOUGH_ARDUINO_STOCK);
        }

        Account account = accountQueryService.findAccount();
        String orderNumber = Order.generateOrderNumber();

        Deposit deposit = Deposit.create(account, DEPOSIT_AMOUNT, null, DepositStatus.REQUIRED);
        Order order = Order.create(orderNumber, account, deposit, OrderStatus.DEPOSIT_PAYMENT_REQUIRED, Subject.valueOf(orderRequest.getSubject().toUpperCase()));
        List<OrderDetail> orderDetailList = OrderDetail.createOrderDetailList(order, cartList);

        Deposit saveDeposit = depositRepository.save(deposit);
        orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetailList);

        PaymentGatewayCheckoutResponse checkoutResponse;

        try {
            checkoutResponse = paymentGateway.checkout(PaymentGatewayCheckoutRequest.create(orderNumber,
                    account, PaymentType.valueOf(orderRequest.getPaymentType().toUpperCase()),
                    PaymentFeature.ARDUINO_DEPOSIT, DEPOSIT_AMOUNT, PAYMENT_QUANTITY));
        } catch (PaymentException exception) {
            throw new OrderException(exception.getMessage() + "다시 한 번 시도해주세요");
        }

        saveDeposit.setPayment(checkoutResponse.getPayment());

        return preProcessOrderResponse.create(orderNumber, checkoutResponse.getMobileRedirectUrl(), checkoutResponse.getPcRedirectUrl());
    }

    // 1. 주문 상태 확인
    // 2. 상태에 따라 무료, 유료 취소
    // 3. 재고 증감
    public OrderCancelResponse orderCancel(String orderNumber) {
        Optional<Order> optionalOrder = orderRepository.findFetchByOrderNumber(orderNumber);

        if (optionalOrder.isEmpty()) {
            throw new OrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER);
        }

        Order order = optionalOrder.get();
        List<OrderDetail> orderDetails = orderDetailRepository.findFetchAllByOrder(order);

        boolean freeCancel = order.isAbleToFreeCancel();

        if (freeCancel) {
            PaymentGatewayRefundResponse refund = paymentGateway.refund(order.getDeposit().getPayment().getPaymentRequestId());
            order.freeCancelOrder();
        } else {
            order.cancelOrder();
        }

        orderDetails.forEach(orderDetail -> orderDetail.getArduino().increaseStock(orderDetail.getOrderArduinoCount()));

        OrderNotificationRequest notificationRequest = OrderNotificationRequest.create(order.getOrderStatus(), order.getDeposit().getDepositStatus(),
                orderNumber, order.getAccount().getId());

        orderNotification.notify(notificationRequest);

        return OrderCancelResponse.create(orderNumber, LocalDateTime.now(),
                order.getOrderStatus().name(), order.getDeposit().getDepositStatus().name(),
                order.getDeposit().getAmount());
    }

}
