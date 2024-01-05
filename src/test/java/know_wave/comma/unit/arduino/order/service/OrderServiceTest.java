package know_wave.comma.unit.arduino.order.service;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountCheckService;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.cart.entity.Cart;
import know_wave.comma.arduino.cart.repository.CartRepository;
import know_wave.comma.arduino.cart.service.CartService;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.notification.dto.OrderNotificationRequest;
import know_wave.comma.arduino.notification.service.ArduinoOrderNotification;
import know_wave.comma.arduino.order.dto.OrderRequest;
import know_wave.comma.arduino.order.dto.preProcessOrderResponse;
import know_wave.comma.arduino.order.entity.*;
import know_wave.comma.arduino.order.repository.DepositRepository;
import know_wave.comma.arduino.order.repository.OrderDetailRepository;
import know_wave.comma.arduino.order.repository.OrderRepository;
import know_wave.comma.arduino.order.service.DepositPaymentCallbackHandler;
import know_wave.comma.arduino.order.service.OrderCommandService;
import know_wave.comma.arduino.order.service.OrderQueryService;
import know_wave.comma.payment.dto.gateway.CompleteCallback;
import know_wave.comma.payment.dto.gateway.CompleteCallbackResponse;
import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutRequest;
import know_wave.comma.payment.dto.gateway.PaymentGatewayCheckoutResponse;
import know_wave.comma.payment.entity.Payment;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentStatus;
import know_wave.comma.payment.entity.PaymentType;
import know_wave.comma.payment.service.PaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("유닛 테스트(서비스) : 아두이노(주문)")
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private AccountQueryService accountQueryService;

    @Mock
    private AccountCheckService accountCheckService;

    @Mock
    private ArduinoOrderNotification orderNotification;

    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private DepositRepository depositRepository;

    @InjectMocks
    private OrderCommandService orderCommandService;

    @InjectMocks
    private OrderQueryService orderQueryService;

    @InjectMocks
    private DepositPaymentCallbackHandler depositPaymentCallbackHandler;

    private static final int TEST_DATA_COUNT = 10;
    private static final int ARDUINO_COUNT = 10;
    private static final List<Arduino> ARDUINOS = new ArrayList<>(TEST_DATA_COUNT);
    private static final List<Cart> CARTS = new ArrayList<>(TEST_DATA_COUNT);
    private static Account ACCOUNT;

    String paymentRequestId;

    OrderRequest orderRequest;

    Deposit deposit;

    Order order;

    List<OrderDetail> orderDetails = new ArrayList<>(TEST_DATA_COUNT);

    Payment payment;

    PaymentGatewayCheckoutResponse paymentGatewayCheckoutResponse;

    PaymentFeature paymentFeature = PaymentFeature.ARDUINO_DEPOSIT;

    String orderNumber = "ORDER_NUMBER";

    @BeforeEach
    void arduinoSetup() {
        ARDUINOS.clear();
        CARTS.clear();
        orderDetails.clear();

        String arduinoName;
        String arduinoDescription;

        // 계정 생성
        ACCOUNT = Account.create("test-user", "test@m365.dongyang.ac.kr", "test-name", "test-password", "01012345678", "20181818", AcademicMajor.AIEngineering);

        // 아두이노 엔티티, 장바구니 생성
        for (int i = 0; i < TEST_DATA_COUNT; i++) {
            arduinoName = "test-arduino" + i;
            arduinoDescription = "test-description" + i;

            Arduino arduino = Arduino.create(arduinoName, ARDUINO_COUNT, arduinoDescription, List.of(Category.createById(1L), Category.createById(2L)));
            ARDUINOS.add(arduino);

            Cart cart = Cart.create(ACCOUNT, arduino, 1);
            CARTS.add(cart);

            orderDetails.add(OrderDetail.create(order, arduino, cart.getStoredCount()));
        }

        paymentRequestId = UUID.randomUUID().toString().substring(0, 20);

        orderRequest = OrderRequest.create("SENIER_PROJECT", "KAKAO_PAY");

        deposit = Deposit.create(ACCOUNT, 100, null, DepositStatus.REQUIRED);

        order = Order.create(orderNumber, ACCOUNT, deposit, OrderStatus.DEPOSIT_PAYMENT_REQUIRED, Subject.valueOf(orderRequest.getSubject().toUpperCase()));

        payment = Payment.create(paymentRequestId, PaymentType.valueOf(orderRequest.getPaymentType()), "TID", paymentFeature, 100, ACCOUNT, 1);

        deposit.setPayment(payment);

        paymentGatewayCheckoutResponse = PaymentGatewayCheckoutResponse.create(payment, "MOBILE_REDIRECT_URL", "PC_REDIRECT_URL", paymentRequestId);
    }

    @DisplayName("주문 전처리")
    @Test
    void givenOrderRequest_whenPreprocessOrder_thenSuccess() {
        // given
        willDoNothing().given(accountCheckService).validateOrderAuthority();

        given(cartService.findCartList()).willReturn(CARTS);

        given(accountQueryService.findAccount()).willReturn(ACCOUNT);

        given(depositRepository.save(ArgumentMatchers.any(Deposit.class))).willReturn(deposit);

        given(paymentGateway.checkout(ArgumentMatchers.any(PaymentGatewayCheckoutRequest.class))).willReturn(paymentGatewayCheckoutResponse);

        // when
        preProcessOrderResponse preProcessOrderResponse = orderCommandService.preProcessOrder(orderRequest);

        // then
        then(depositRepository).should().save(ArgumentMatchers.any(Deposit.class));
        then(orderRepository).should().save(ArgumentMatchers.any(Order.class));
        then(orderDetailRepository).should().saveAll(anyList());

        assertThat(deposit.getPayment()).isEqualTo(payment);
        assertThat(preProcessOrderResponse.getMobileRedirectUrl()).isEqualTo(paymentGatewayCheckoutResponse.getMobileRedirectUrl());
        assertThat(preProcessOrderResponse.getPcRedirectUrl()).isEqualTo(paymentGatewayCheckoutResponse.getPcRedirectUrl());
    }

    @DisplayName("주문 콜백 처리")
    @Test
    void givenOrderApprove_whenOrderCallback_thenOrdered() {
        //given
        payment.setPaymentStatus(PaymentStatus.COMPLETE);

        CompleteCallback completeCallback = CompleteCallback.create(paymentRequestId, "ORDER_NUMBER", ACCOUNT.getId(), paymentFeature);

        given(orderRepository.findFetchByOrderNumber(anyString())).willReturn(java.util.Optional.of(order));
        given(orderDetailRepository.findFetchAllByOrder(any(Order.class))).willReturn(orderDetails);

        // 테스트용 아두이노 객체에 id 값을 넣을 수 없으므로 주문 성공 후 장바구니 비우기는 테스트하지 않음
        given(cartRepository.findAllByAccount(any(Account.class))).willReturn(Collections.EMPTY_LIST);

        //when
        CompleteCallbackResponse callbackResponse = depositPaymentCallbackHandler.complete(completeCallback);

        //then
        then(orderRepository).should().findFetchByOrderNumber(anyString());
        then(orderDetailRepository).should().findFetchAllByOrder(any(Order.class));
        then(cartRepository).should().findAllByAccount(any(Account.class));
        then(orderNotification).should().notify(any(OrderNotificationRequest.class));

        Map<String, String> completeCallbackResult = callbackResponse.getCompleteCallbackResult();

        assertThat(completeCallbackResult.get("orderStatus")).isEqualTo(OrderStatus.ORDERED.getStatus());
        assertThat(completeCallbackResult.get("depositStatus")).isEqualTo(DepositStatus.PAID.getStatus());
    }

}
