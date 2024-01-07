package know_wave.comma.unit.payment.service;

import know_wave.comma.account.entity.AcademicMajor;
import know_wave.comma.account.entity.Account;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.idempotency.dto.IdempotentRequest;
import know_wave.comma.common.idempotency.dto.IdempotentSaveDto;
import know_wave.comma.common.idempotency.service.IdempotencyService;
import know_wave.comma.payment.dto.client.PaymentClientApproveResponse;
import know_wave.comma.payment.dto.client.PaymentClientReadyResponse;
import know_wave.comma.payment.dto.client.PaymentClientRefundRequest;
import know_wave.comma.payment.dto.client.PaymentClientRefundResponse;
import know_wave.comma.payment.dto.gateway.*;
import know_wave.comma.payment.entity.Payment;
import know_wave.comma.payment.entity.PaymentFeature;
import know_wave.comma.payment.entity.PaymentStatus;
import know_wave.comma.payment.entity.PaymentType;
import know_wave.comma.payment.exception.PaymentCheckoutException;
import know_wave.comma.payment.exception.PaymentClient4xxException;
import know_wave.comma.payment.exception.PaymentClient5xxException;
import know_wave.comma.payment.exception.PaymentRefundException;
import know_wave.comma.payment.repository.PaymentRepository;
import know_wave.comma.payment.service.PaymentCallbackManager;
import know_wave.comma.payment.service.PaymentClientManager;
import know_wave.comma.payment.service.PaymentGateway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@DisplayName("유닛 테스트(서비스) : 결제(게이트웨이)")
@ExtendWith(MockitoExtension.class)
public class PaymentGatewayTest {

    @Mock
    private IdempotencyService idempotencyService;

    @Mock
    private PaymentClientManager paymentClientManager;

    @Mock
    private PaymentCallbackManager paymentCallbackManager;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentGateway paymentGateway;

    private static Account ACCOUNT;

    private static String paymentRequestId;

    private Payment payment;

    private static final String orderNumber = "testOrderNumber";
    private static final PaymentType PAYMENT_TYPE = PaymentType.KAKAO_PAY;
    private static final PaymentFeature PAYMENT_FEATURE = PaymentFeature.ARDUINO_DEPOSIT;
    private static final String TID = "testTid";
    private static final String CID = "testCid";
    private static final String pgToken = "testPgToken";
    private static final String ITEM_NAME = "testItemName";
    private static final int AMOUNT = 100;
    private static final int QUANTITY = 1;

    @BeforeAll
    static void setUp() {
        ACCOUNT = Account.create("test-user", "test@m365.dongyang.ac.kr", "test-name", "test-password", "01012345678", "20181818", AcademicMajor.AIEngineering);
        paymentRequestId = Payment.generatePaymentRequestId(ACCOUNT.getId(), orderNumber);
    }

    @BeforeEach
    void init() {
        payment = Payment.create(paymentRequestId, PAYMENT_TYPE, TID, PAYMENT_FEATURE, AMOUNT, ACCOUNT, QUANTITY);
    }

    @DisplayName("결제 요청")
    @Test
    void checkout() {
        //given
        PaymentGatewayCheckoutRequest paymentGatewayCheckoutRequest =
                PaymentGatewayCheckoutRequest.create(orderNumber, ACCOUNT, PAYMENT_TYPE, PAYMENT_FEATURE, AMOUNT, QUANTITY);

        PaymentClientReadyResponse paymentClientReadyResponse = PaymentClientReadyResponse.create("MOBILE_REDIRECT_URL", "PC_REDIRECT_URL", "TID");

        given(idempotencyService.isIdempotent(ArgumentMatchers.any(IdempotentRequest.class))).willReturn(false);

        given(paymentClientManager.ready(ArgumentMatchers.any(PaymentGatewayCheckoutRequest.class), ArgumentMatchers.anyString()))
                .willReturn(paymentClientReadyResponse);

        given(paymentRepository.save(ArgumentMatchers.any(Payment.class))).willReturn(payment);

        willDoNothing().given(idempotencyService).create(ArgumentMatchers.any(IdempotentSaveDto.class));

        //when
        PaymentGatewayCheckoutResponse checkout = paymentGateway.checkout(paymentGatewayCheckoutRequest);

        //then
        assertThat(checkout.getPayment().getPaymentStatus()).isEqualTo(PaymentStatus.REQUEST);
        assertThat(checkout.getPayment().getPaymentType()).isEqualTo(PAYMENT_TYPE);
        assertThat(checkout.getPayment().getPaymentFeature()).isEqualTo(PAYMENT_FEATURE);
        assertThat(checkout.getPayment().getAmount()).isEqualTo(AMOUNT);
        assertThat(checkout.getPayment().getQuantity()).isEqualTo(QUANTITY);
        assertThat(checkout.getPayment().getAccount()).isEqualTo(ACCOUNT);

        assertThat(checkout.getMobileRedirectUrl()).isEqualTo(paymentClientReadyResponse.getMobileRedirectUrl()).isEqualTo("MOBILE_REDIRECT_URL");
        assertThat(checkout.getPcRedirectUrl()).isEqualTo(paymentClientReadyResponse.getPcRedirectUrl()).isEqualTo("PC_REDIRECT_URL");

    }

    @DisplayName("결제 승인")
    @Test
    void approve() {
        //given
        PaymentGatewayApproveRequest paymentGatewayApproveRequest =
                PaymentGatewayApproveRequest.create(paymentRequestId, orderNumber, ACCOUNT.getId(), PAYMENT_TYPE, PAYMENT_FEATURE, pgToken);

        PaymentClientApproveResponse paymentClientApproveResponse = PaymentClientApproveResponse.create(TID, CID, paymentRequestId, ACCOUNT.getId(), AMOUNT, QUANTITY, ITEM_NAME, LocalDateTime.now(), LocalDateTime.now());

        CompleteCallbackResponse completeCallbackResponse = CompleteCallbackResponse.create(Map.of("result", "CompleteCallbackResult"));

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentClientManager.approve(ArgumentMatchers.any(PaymentGatewayApproveRequest.class), ArgumentMatchers.anyString()))
                .willReturn(paymentClientApproveResponse);

        given(paymentCallbackManager.complete(ArgumentMatchers.any())).willReturn(completeCallbackResponse);

        //when
        paymentGateway.approve(paymentGatewayApproveRequest);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETE);

        verify(paymentRepository, times(1)).findByPaymentRequestId(paymentRequestId);
    }

    @DisplayName("결제 중 취소")
    @Test
    void cancel() {
        //given
        PaymentGatewayCancelRequest paymentGatewayCancelRequest = PaymentGatewayCancelRequest.create(paymentRequestId, orderNumber, ACCOUNT.getId(), PAYMENT_TYPE, PAYMENT_FEATURE);

        CancelCallbackResponse cancelCallbackResponse = CancelCallbackResponse.create(Map.of("result", "CancelCallbackResult"));

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentCallbackManager.cancel(ArgumentMatchers.any())).willReturn(cancelCallbackResponse);

        //when
        paymentGateway.cancel(paymentGatewayCancelRequest);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.CANCEL);
        verify(paymentRepository, times(1)).findByPaymentRequestId(paymentRequestId);
    }

    @DisplayName("결제 중 실패")
    @Test
    void fail() {
        //given
        PaymentGatewayFailRequest paymentGatewayFailRequest = PaymentGatewayFailRequest.create(paymentRequestId, orderNumber, ACCOUNT.getId(), PAYMENT_TYPE, PAYMENT_FEATURE);

        FailCallbackResponse cancelCallbackResponse = FailCallbackResponse.create(Map.of("result", "FailCallbackResult"));

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentCallbackManager.fail(ArgumentMatchers.any())).willReturn(cancelCallbackResponse);

        //when
        paymentGateway.fail(paymentGatewayFailRequest);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILURE);

        verify(paymentRepository, times(1)).findByPaymentRequestId(paymentRequestId);
    }

    @DisplayName("결제 환불")
    @Test
    void refund() {
        //given

        PaymentClientRefundResponse paymentClientRefundResponse =
                PaymentClientRefundResponse.create(paymentRequestId, CID, paymentRequestId, ACCOUNT.getId(), PaymentStatus.REFUND.getValue(), AMOUNT, AMOUNT, QUANTITY, ITEM_NAME, LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentClientManager.refund(ArgumentMatchers.any())).willReturn(paymentClientRefundResponse);

        //when
        PaymentGatewayRefundResponse refund = paymentGateway.refund(paymentRequestId);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REFUND);
        assertThat(refund.getPaymentRequestId()).isEqualTo(paymentRequestId);
        assertThat(refund.getPaymentFeature()).isEqualTo(PAYMENT_FEATURE);
        assertThat(refund.getPaymentType()).isEqualTo(PAYMENT_TYPE);

        verify(paymentRepository, times(1)).findByPaymentRequestId(paymentRequestId);
    }

    @DisplayName("결제 요청 실패(외부 api 4xx 에러)")
    @Test
    void givenExternalApi4xxError_whenCheckout_thenFailure() {
        //given
        String errorMessage = "testErrorMessage";
        HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;
        int errorCode = -123123;

        String paymentErrorMessage = ExceptionMessageSource.INVALID_PAYMENT_CHECKOUT_REQUEST;

        PaymentGatewayCheckoutRequest paymentGatewayCheckoutRequest = PaymentGatewayCheckoutRequest.create(orderNumber, ACCOUNT, PAYMENT_TYPE, PAYMENT_FEATURE, AMOUNT, QUANTITY);

        given(idempotencyService.isIdempotent(ArgumentMatchers.any(IdempotentRequest.class))).willReturn(false);

        given(paymentClientManager.ready(ArgumentMatchers.any(PaymentGatewayCheckoutRequest.class), ArgumentMatchers.anyString()))
                .willThrow(new PaymentClient4xxException(errorMessage, httpStatusCode, errorCode));

        //when & then
        assertThatThrownBy(() -> paymentGateway.checkout(paymentGatewayCheckoutRequest))
                .isInstanceOf(PaymentCheckoutException.class)
                .hasMessage(paymentErrorMessage);

        verify(idempotencyService, times(1)).isIdempotent(ArgumentMatchers.any(IdempotentRequest.class));
        verify(paymentClientManager, times(1)).ready(ArgumentMatchers.any(PaymentGatewayCheckoutRequest.class), ArgumentMatchers.anyString());
    }

    @DisplayName("결제 요청 실패(외부 api 5xx 에러)")
    @Test
    void givenExternalApi5xxError_whenCheckout_thenFailure() {
        //given
        String errorMessage = "testErrorMessage";
        HttpStatusCode httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        int errorCode = -123123;

        String paymentErrorMessage = ExceptionMessageSource.PAYMENT_SERVER_ERROR;

        PaymentGatewayCheckoutRequest paymentGatewayCheckoutRequest = PaymentGatewayCheckoutRequest.create(orderNumber, ACCOUNT, PAYMENT_TYPE, PAYMENT_FEATURE, AMOUNT, QUANTITY);

        given(idempotencyService.isIdempotent(ArgumentMatchers.any(IdempotentRequest.class))).willReturn(false);

        given(paymentClientManager.ready(ArgumentMatchers.any(PaymentGatewayCheckoutRequest.class), ArgumentMatchers.anyString()))
                .willThrow(new PaymentClient5xxException(errorMessage, httpStatusCode, errorCode));

        //when & then
        assertThatThrownBy(() -> paymentGateway.checkout(paymentGatewayCheckoutRequest))
                .isInstanceOf(PaymentCheckoutException.class)
                .hasMessage(paymentErrorMessage);

        verify(idempotencyService, times(1)).isIdempotent(ArgumentMatchers.any(IdempotentRequest.class));
        verify(paymentClientManager, times(1)).ready(ArgumentMatchers.any(PaymentGatewayCheckoutRequest.class), ArgumentMatchers.anyString());
    }

    @DisplayName("결제 승인 실패(외부 api 4xx 에러)")
    @Test
    void givenExternalApi4xxError_whenApprove_thenFailure() {
        //given
        String errorMessage = "testErrorMessage";
        HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;
        int errorCode = -123123;

        PaymentGatewayApproveRequest paymentGatewayApproveRequest = PaymentGatewayApproveRequest.create(paymentRequestId, orderNumber, ACCOUNT.getId(), PAYMENT_TYPE, PAYMENT_FEATURE, pgToken);

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentClientManager.approve(ArgumentMatchers.any(PaymentGatewayApproveRequest.class), ArgumentMatchers.anyString()))
                .willThrow(new PaymentClient4xxException(errorMessage, httpStatusCode, errorCode));

        willDoNothing().given(paymentCallbackManager).error(ArgumentMatchers.any(ErrorCallback.class));

        //when
        paymentGateway.approve(paymentGatewayApproveRequest);

        ArgumentCaptor<ErrorCallback> errorCallbackArgumentCaptor = ArgumentCaptor.forClass(ErrorCallback.class);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILURE);
        verify(paymentCallbackManager, times(1)).error(errorCallbackArgumentCaptor.capture());
        ErrorCallback errorCallback = errorCallbackArgumentCaptor.getValue();
        assertThat(errorCallback.getErrorCode()).isEqualTo(errorCode);
        assertThat(errorCallback.getErrorMessage()).isEqualTo(errorMessage);
        assertThat(errorCallback.getStatusCode()).isEqualTo(httpStatusCode);
    }

    @DisplayName("결제 승인 실패(외부 api 5xx 에러)")
    @Test
    void givenExternalApi5xxError_whenApprove_thenFailure() {
        //given
        String errorMessage = "testErrorMessage";
        HttpStatusCode httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        int errorCode = -123123;

        PaymentGatewayApproveRequest paymentGatewayApproveRequest = PaymentGatewayApproveRequest.create(paymentRequestId, orderNumber, ACCOUNT.getId(), PAYMENT_TYPE, PAYMENT_FEATURE, pgToken);
        ArgumentCaptor<ErrorCallback> errorCallbackArgumentCaptor = ArgumentCaptor.forClass(ErrorCallback.class);

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentClientManager.approve(ArgumentMatchers.any(PaymentGatewayApproveRequest.class), ArgumentMatchers.anyString()))
                .willThrow(new PaymentClient5xxException(errorMessage, httpStatusCode, errorCode));

        willDoNothing().given(paymentCallbackManager).error(ArgumentMatchers.any(ErrorCallback.class));

        //when
        paymentGateway.approve(paymentGatewayApproveRequest);

        //then
        assertThatThrownBy(() -> paymentClientManager.approve(paymentGatewayApproveRequest, "testTid"))
                .isInstanceOf(PaymentClient5xxException.class);

        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.FAILURE);

        verify(paymentCallbackManager, times(1)).error(errorCallbackArgumentCaptor.capture());
        ErrorCallback errorCallback = errorCallbackArgumentCaptor.getValue();

        assertThat(errorCallback.getErrorCode()).isEqualTo(errorCode);
        assertThat(errorCallback.getErrorMessage()).isEqualTo(errorMessage);
        assertThat(errorCallback.getStatusCode()).isEqualTo(httpStatusCode);
    }

    @DisplayName("보증금 반환 실패(외부 api 4xx 에러)")
    @Test
    void givenExternalApi4xxError_whenReturn_thenFailure() {
        //given
        String errorMessage = "testErrorMessage";
        HttpStatusCode httpStatusCode = HttpStatus.BAD_REQUEST;
        int errorCode = -123123;

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentClientManager.refund(ArgumentMatchers.any(PaymentClientRefundRequest.class)))
                .willThrow(new PaymentClient4xxException(errorMessage, httpStatusCode, errorCode));

        //when
        assertThatThrownBy(() -> paymentGateway.refund(paymentRequestId))
                .isInstanceOf(PaymentRefundException.class);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REQUIRED_REFUND);
    }

    @DisplayName("보증금 반환 실패(외부 api 5xx 에러)")
    @Test
    void givenExternalApi5xxError_whenReturn_thenFailure() {
        //given
        String errorMessage = "testErrorMessage";
        HttpStatusCode httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        int errorCode = -123123;

        given(paymentRepository.findByPaymentRequestId(paymentRequestId)).willReturn(Optional.of(payment));

        given(paymentClientManager.refund(ArgumentMatchers.any(PaymentClientRefundRequest.class)))
                .willThrow(new PaymentClient5xxException(errorMessage, httpStatusCode, errorCode));

        //when
        assertThatThrownBy(() -> paymentGateway.refund(paymentRequestId))
                .isInstanceOf(PaymentRefundException.class);

        //then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.REQUIRED_REFUND);
    }

}
