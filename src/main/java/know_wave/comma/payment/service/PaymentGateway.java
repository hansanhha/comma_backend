package know_wave.comma.payment.service;

import jakarta.transaction.Transactional;
import know_wave.comma.common.idempotency.dto.IdempotentRequest;
import know_wave.comma.common.idempotency.dto.IdempotentSaveDto;
import know_wave.comma.common.idempotency.service.IdempotencyService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.payment.dto.client.PaymentClientApproveResponse;
import know_wave.comma.payment.dto.client.PaymentClientReadyResponse;
import know_wave.comma.payment.dto.client.PaymentClientRefundRequest;
import know_wave.comma.payment.dto.client.PaymentClientRefundResponse;
import know_wave.comma.payment.dto.gateway.*;
import know_wave.comma.payment.entity.Payment;
import know_wave.comma.payment.entity.PaymentStatus;
import know_wave.comma.payment.exception.*;
import know_wave.comma.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentGateway {

    private final IdempotencyService idempotencyService;
    private final PaymentClientManager paymentClientManager;
    private final PaymentCallbackManager paymentCallbackManager;
    private final PaymentRepository paymentRepository;

    /*
     *  paymentRequestId : 결제 식별, 멱등키(idempotentKey), 외부 api 주문번호 id로도 사용
     *  orderNumber : 결제 기능을 요청한 도메인의 주문번호
     *  tid : 외부 결제 API 서버에서 결제를 식별하는 키 (외부 API 사용 용도)
     *  pgToken : 결제 수단을 식별하는 키 (외부 API 사용 용도)
     *  checkout()은 다른 도메인 서비스 계층에서 호출되므로 멱등성 검증, 생성을 메서드 내부에서 처리
     *  이외의 메서드는 컨트롤러에서 처리
     *  특정 도메인의 서비스 객체 -> 결제 checkout() 호출 ->
     *  결제 콜백 api 처리 및 응답 -> 응답 결과에 따라 클라이언트에서 서비스 객체 호출(로직 재개)
     */
    public PaymentGatewayCheckoutResponse checkout(PaymentGatewayCheckoutRequest checkoutRequest) {
        String paymentRequestId = Payment.generatePaymentRequestId(checkoutRequest.getAccount().getId(), checkoutRequest.getOrderNumber());

        Optional<PaymentClientReadyResponse> optionalIdempotentResponse =
                getIdempotentResponse(paymentRequestId);

        if (optionalIdempotentResponse.isPresent()) {
            PaymentClientReadyResponse paymentReadyResponse = optionalIdempotentResponse.get();
            Payment payment = getPayment(paymentRequestId);
            return PaymentGatewayCheckoutResponse.create(payment, paymentReadyResponse.getMobileRedirectUrl(), paymentReadyResponse.getPcRedirectUrl(), null);
        }

        PaymentClientReadyResponse paymentClientReadyResponse;

        try {
            paymentClientReadyResponse = paymentClientManager.ready(checkoutRequest, paymentRequestId);
        } catch (PaymentClient4xxException exception4) {
            throw new PaymentCheckoutException(ExceptionMessageSource.INVALID_PAYMENT_CHECKOUT_REQUEST);
        } catch (PaymentClient5xxException exception5) {
            throw new PaymentCheckoutException(ExceptionMessageSource.PAYMENT_SERVER_ERROR);
        } catch (PaymentClientUnknownException unknownException) {
            throw new PaymentCheckoutException(ExceptionMessageSource.RETRY_PAYMENT_CHECKOUT_REQUEST);
        }

        Payment payment = Payment.create(
                paymentRequestId, checkoutRequest.getPaymentType(), paymentClientReadyResponse.getTid(),
                checkoutRequest.getPaymentFeature(), checkoutRequest.getAmount(), checkoutRequest.getAccount(),
                checkoutRequest.getQuantity());

        Payment savedPayment = paymentRepository.save(payment);
        idempotencyService.create(IdempotentSaveDto.create(paymentRequestId, null, null, 0, paymentClientReadyResponse, null));

        return PaymentGatewayCheckoutResponse.create(savedPayment, paymentClientReadyResponse.getMobileRedirectUrl(), paymentClientReadyResponse.getPcRedirectUrl(), paymentRequestId);
    }

    public PaymentGatewayApproveResponse approve(PaymentGatewayApproveRequest approveRequest) {
        Payment payment = getPayment(approveRequest.getPaymentRequestId());
        PaymentClientApproveResponse approve = null;

        try {
            approve = paymentClientManager.approve(approveRequest, payment.getExternalApiTransactionId());
        } catch (PaymentClientException ex) {
            ErrorCallback errorCallback = ErrorCallback.create(approveRequest.getPaymentRequestId(), approveRequest.getOrderNumber(),
                    approveRequest.getAccountId(), approveRequest.getPaymentFeature(), ex.getHttpStatusCode(), ex.getErrorCode(), ex.getMessage());

            paymentCallbackManager.error(errorCallback);
        }

        payment.setPaymentStatus(PaymentStatus.COMPLETE);

        CompleteCallbackResponse callbackResponse = paymentCallbackManager.complete(
                CompleteCallback.create(approveRequest.getPaymentRequestId(), approveRequest.getOrderNumber(),
                        approveRequest.getAccountId(), approveRequest.getPaymentFeature()));

        return PaymentGatewayApproveResponse.create(
                callbackResponse.getCompleteCallbackResult(), approveRequest.getPaymentRequestId(), approveRequest.getOrderNumber(),
                approve.getPayerId(), payment.getPaymentStatus().getValue(), payment.getPaymentFeature().getFeature(),
                payment.getPaymentType().getType(), approve.getAmount(), approve.getQuantity(),
                approve.getPaymentReadyDate(), approve.getPaymentApproveDate());
    }

    public PaymentGatewayCancelResponse cancel(PaymentGatewayCancelRequest cancelRequest) {
        Payment payment = getPayment(cancelRequest.getPaymentRequestId());
        payment.setPaymentStatus(PaymentStatus.CANCEL);

        CancelCallbackResponse callbackResponse = paymentCallbackManager.cancel(
                CancelCallback.of(cancelRequest.getPaymentRequestId(), cancelRequest.getOrderNumber(),
                        cancelRequest.getAccountId(), cancelRequest.getPaymentFeature()));

        return PaymentGatewayCancelResponse.create(
                callbackResponse.getCancelCallbackResult(), cancelRequest.getPaymentRequestId(), cancelRequest.getOrderNumber(),
                cancelRequest.getAccountId(), payment.getAmount(), payment.getQuantity(),
                payment.getPaymentStatus().getValue(), payment.getPaymentFeature().getFeature(), payment.getPaymentType().getType());
    }

    public PaymentGatewayFailResponse fail(PaymentGatewayFailRequest failRequest) {
        Payment payment = getPayment(failRequest.getPaymentRequestId());
        payment.setPaymentStatus(PaymentStatus.FAILURE);

        FailCallbackResponse callbackResponse = paymentCallbackManager.fail(
                FailCallback.of(failRequest.getPaymentRequestId(), failRequest.getOrderNumber(),
                        failRequest.getAccountId(), failRequest.getPaymentFeature()));

        return PaymentGatewayFailResponse.create(
                callbackResponse.getFailCallbackResult(), failRequest.getPaymentRequestId(), failRequest.getOrderNumber(),
                failRequest.getAccountId(), payment.getAmount(), payment.getQuantity(),
                payment.getPaymentStatus().getValue(), payment.getPaymentFeature().getFeature(), payment.getPaymentType().getType());
    }

    public PaymentGatewayRefundResponse refund(String paymentRequestId) {
        Payment payment = getPayment(paymentRequestId);

        PaymentClientRefundRequest refundRequest = PaymentClientRefundRequest.create(payment.getExternalApiTransactionId(),
                payment.getAmount(), payment.getPaymentType());

        PaymentClientRefundResponse refund = paymentClientManager.refund(refundRequest);

        payment.setPaymentStatus(PaymentStatus.REFUND);

        return PaymentGatewayRefundResponse.create(
                paymentRequestId, refund.getPayerId(), payment.getPaymentStatus(),
                payment.getPaymentFeature(), payment.getPaymentType(), refund.getAmount(),
                refund.getCancelAmount(), refund.getQuantity(), refund.getItemName(),
                refund.getPaymentReadyDate(), refund.getPaymentApproveDate(), refund.getPaymentCancelDate()
        );
    }

    private Optional<PaymentClientReadyResponse> getIdempotentResponse(String paymentRequestId) {
        IdempotentRequest idempotentRequest = IdempotentRequest.create(paymentRequestId, null, null, null);

        if (idempotencyService.isIdempotent(idempotentRequest)) {
            var idempotentResponse = idempotencyService.get(paymentRequestId, PaymentClientReadyResponse.class);
            if (idempotentResponse.isPresent()) {
                return Optional.of(idempotentResponse.get().getResponse());
            }
        }

        return Optional.empty();
    }

    private Payment getPayment(String paymentRequestId) {
        return paymentRepository.findByPaymentRequestId(paymentRequestId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_PAYMENT_INFO));
    }

}
