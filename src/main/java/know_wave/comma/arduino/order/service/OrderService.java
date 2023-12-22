package know_wave.comma.arduino.order.service;

import jakarta.transaction.Transactional;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountCheckService;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.basket.dto.BasketValidateStatus;
import know_wave.comma.arduino.basket.entity.Basket;
import know_wave.comma.arduino.basket.exception.BasketException;
import know_wave.comma.arduino.basket.service.BasketService;
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
import know_wave.comma.payment.service.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


/* TODO
    * 주문 수령 이후 보증금 결제 환불 처리
 */
@Service
@Transactional
@RequiredArgsConstructor
@CheckAccountStatus
public class OrderService {

    private final BasketService basketService;
    private final AccountQueryService accountQueryService;
    private final AccountCheckService accountCheckService;
    private final PaymentGateway paymentGateway;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DepositRepository depositRepository;
    private static final int DEPOSIT_AMOUNT = 3000;
    private static final int PAYMENT_QUANTITY = 1;
    @Value("${arduino.max-order-quantity}")
    private int ORDER_MAX_QUANTITY;

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

        List<Basket> basketList = basketService.findBasketList();

        if (basketList.isEmpty()) {
            throw new BasketException(ExceptionMessageSource.EMPTY_BASKET);
        }

        BasketValidateStatus basketValidateStatus = Basket.validateList(basketList, ORDER_MAX_QUANTITY);

        if (basketValidateStatus.isOverMaxQuantity()) {
            throw new OrderException(ExceptionMessageSource.OVER_MAX_ARDUINO_QUANTITY);
        } else if (basketValidateStatus.isBadArduinoStatus()) {
            throw new OrderException(ExceptionMessageSource.BAD_ARDUINO_STOCK_STATUS);
        } else if (basketValidateStatus.isNotEnoughStock()) {
            throw new OrderException(ExceptionMessageSource.NOT_ENOUGH_ARDUINO_STOCK);
        }

        Account account = accountQueryService.findAccount();
        String orderNumber = Order.generateOrderNumber();

        Deposit deposit = Deposit.create(account, DEPOSIT_AMOUNT, null, DepositStatus.REQUIRED);
        Order order = Order.create(orderNumber, account, deposit, OrderStatus.DEPOSIT_PAYMENT_REQUIRED, Subject.valueOf(orderRequest.getSubject()));
        List<OrderDetail> orderDetailList = OrderDetail.createOrderDetailList(order, basketList);

        Deposit saveDeposit = depositRepository.save(deposit);
        orderRepository.save(order);
        orderDetailRepository.saveAll(orderDetailList);

        PaymentGatewayCheckoutResponse checkoutResponse = paymentGateway.checkout(PaymentGatewayCheckoutRequest.of(orderNumber,
                account, PaymentType.valueOf(orderRequest.getPaymentType()),
                PaymentFeature.ARDUINO_DEPOSIT, DEPOSIT_AMOUNT, PAYMENT_QUANTITY));

        saveDeposit.setPayment(checkoutResponse.getPayment());

        return preProcessOrderResponse.to(orderNumber, checkoutResponse.getMobileRedirectUrl(), checkoutResponse.getPcRedirectUrl());
    }

    // 1. 주문 상태 확인
    // 2. 상태에 따라 무료, 유료 취소
    // 3. 재고 증감
    public OrderCancelResponse orderCancel(String orderNumber) {
        Order order = findOrderByOrderNumber(orderNumber);
        List<OrderDetail> orderDetails = findOrderDetails(order);

        boolean freeCancel = order.isAbleToFreeCancel();

        if (freeCancel) {
            PaymentGatewayRefundResponse refund = paymentGateway.refund(order.getDeposit().getPayment().getPaymentRequestId());
            order.freeCancelOrder();
        } else {
            order.cancelOrder();
        }

        orderDetails.forEach(orderDetail -> orderDetail.getArduino().increaseStock(orderDetail.getOrderArduinoCount()));

        return OrderCancelResponse.to(orderNumber, LocalDateTime.now(),
                order.getOrderStatus().name(), order.getDeposit().getDepositStatus().name(),
                order.getDeposit().getAmount());
    }

    public OrderPageResponse getOrderPage(Pageable pageable) {
        Account account = accountQueryService.findAccount();

        Page<Order> orders = orderRepository.findAllByAccount(account, pageable);
        List<OrderResponse> orderResponses = orders.stream().map(OrderResponse::to).toList();

        return OrderPageResponse.to(orderResponses,
                orders.isFirst(), orders.isLast(), orders.hasNext(), orders.getSize());
    }

    public OrderDetailResponse getOrderDetail(String orderNumber) {
        Order order = findOrderByOrderNumber(orderNumber);
        List<OrderDetail> orderDetails = findOrderDetails(order);

        return OrderDetailResponse.to(order, orderDetails);
    }

    private Order findOrderByOrderNumber(String orderNumber) {
        return orderRepository.findFetchByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER));
    }

    private List<OrderDetail> findOrderDetails(Order order) {
        return orderDetailRepository.findFetchAllByOrder(order);
    }

}
