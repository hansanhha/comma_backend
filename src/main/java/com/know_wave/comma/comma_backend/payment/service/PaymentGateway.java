package com.know_wave.comma.comma_backend.payment.service;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.order.dto.OrderInfoDto;
import com.know_wave.comma.comma_backend.order.entity.OrderInfo;
import com.know_wave.comma.comma_backend.order.service.user.OrderInfoQueryService;
import com.know_wave.comma.comma_backend.common.idempotency.Idempotency;
import com.know_wave.comma.comma_backend.common.idempotency.Idempotent;
import com.know_wave.comma.comma_backend.common.idempotency.IdempotentDto;
import com.know_wave.comma.comma_backend.common.idempotency.IdempotentKeyRepository;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareDto;
import com.know_wave.comma.comma_backend.payment.dto.PaymentPrepareResponse;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundRequest;
import com.know_wave.comma.comma_backend.payment.dto.PaymentRefundResult;
import com.know_wave.comma.comma_backend.payment.entity.Deposit;
import com.know_wave.comma.comma_backend.payment.entity.DepositStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentStatus;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import com.know_wave.comma.comma_backend.payment.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Idempotency
@Transactional
@RequiredArgsConstructor
public class PaymentGateway {

    private final AccountQueryService accountQueryService;
    private final DepositQueryService depositQueryService;
    private final OrderInfoQueryService orderInfoQueryService;
    private final PaymentManager paymentManager;
    private final DepositRepository depositRepository;
    private final IdempotentKeyRepository idempotentKeyRepository;
    private final CommaArduinoDepositPolicy depositPolicy;

    // api/v1/payment/*/*/paymentRequestId/idempotencyKey
    // 첫 번째 %s - paymentType : 결제 수단
    // 두 번째 %s - paymentRequestId : PG, 간편결제에서 결제 요청(결제 준비) 후 redirect 됐을 때 해당 결제건 식별 용도
    // 세 번째 %s - idempotencyKey : 멱등성 유지를 위한 키 값

    // failUrl, cancelUrl 네 번째 %s - orderNumber : 주문 번호 (결제 실패, 취소 시 로직 처리 용도)
    // failUrl, cancelUrl 다섯 번째 %s - emitterId : SSE 통신을 위한 emitterId

    // successUrl 네 번째 %s - accountId : 사용자 계정 (결제 성공 시 주문 기능 실행 용도)
    // successUrl 다섯 번째 %s - orderNumber : 주문 번호 (결제 성공 시 주문 기능 실행 용도)
    // successUrl 여섯 번째 %s - subject : 주문 교과목 (결제 성공 시 주문 기능 실행 용도)
    // successUrl 일곱 번째 %s - emitterId : SSE 통신을 위한 emitterId
    public static final String successUrl = "http://localhost:8080/api/v1/payment/%s/success/%s/%s/%s/%s/%s/%s";
    public static final String failUrl = "http://localhost:8080/api/v1/payment/%s/fail/%s/%s/%s/%s";
    public static final String cancelUrl = "http://localhost:8080/api/v1/payment/%s/cancel/%s/%s/%s/%s";
    
    public PaymentPrepareResponse prepare(IdempotentDto idempotentDto, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfoDto) {
        var paymentService = paymentManager.getPaymentService(paymentPrepareDto.paymentType());
        var paymentPrepareResult = paymentService.ready(idempotentDto.idempotentKey(), paymentPrepareDto, orderInfoDto);
        var paymentPrepareResponse = new PaymentPrepareResponse(paymentPrepareResult.redirectPcWebUrl(), paymentPrepareResult.redirectMobileWebUrl());

        Account account = accountQueryService.findAccount(AccountQueryService.getAuthenticatedId());
        Deposit deposit = Deposit.of(paymentPrepareDto.paymentType(), paymentPrepareResult.paymentRequestId(), paymentPrepareResult.transactionId(), account, depositPolicy.getAmount(), depositPolicy.getProductName(), true, true);
        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), paymentPrepareResponse);

        depositRepository.save(deposit);
        idempotentKeyRepository.save(idempotent);

        return paymentPrepareResponse;
    }

    public PaymentPrepareResponse prepareWithSSE(IdempotentDto idempotentDto, PaymentPrepareDto paymentPrepareDto, OrderInfoDto orderInfoDto, String sseId) {
        var paymentService = paymentManager.getPaymentService(paymentPrepareDto.paymentType());
        var paymentPrepareResult = paymentService.readyWithSSE(idempotentDto.idempotentKey(), paymentPrepareDto, orderInfoDto, sseId);
        var paymentPrepareResponse = new PaymentPrepareResponse(paymentPrepareResult.redirectPcWebUrl(), paymentPrepareResult.redirectMobileWebUrl());

        Account account = accountQueryService.findAccount(AccountQueryService.getAuthenticatedId());
        Deposit deposit = Deposit.of(paymentPrepareDto.paymentType(), paymentPrepareResult.paymentRequestId(), paymentPrepareResult.transactionId(), account, depositPolicy.getAmount(), depositPolicy.getProductName(), true, true);
        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), paymentPrepareResponse);

        depositRepository.save(deposit);
        idempotentKeyRepository.save(idempotent);

        return paymentPrepareResponse;
    }

    public Long approve(IdempotentDto idempotentDto, String paymentType, String paymentRequestId, String tempOrderNumber, String paymentToken) {

        PaymentType paymentTypeUpperCase = PaymentType.valueOf(paymentType.toUpperCase());
        var paymentService = paymentManager.getPaymentService(paymentTypeUpperCase);
        Deposit deposit = depositQueryService.getDepositByRequestId(paymentRequestId);

        paymentService.pay(deposit, tempOrderNumber, paymentToken);

        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), "already paid deposit");
        idempotentKeyRepository.save(idempotent);

        deposit.setPaymentStatus(PaymentStatus.APPROVE);
        deposit.setDepositStatus(DepositStatus.PAID);

        return deposit.getId();
    }

    public void refund(IdempotentDto idempotentDto, PaymentRefundRequest request) {
        var paymentService = paymentManager.getPaymentService(request.getPaymentType());

        Deposit deposit = depositQueryService.getDepositById(request.getPaymentId());
        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), "already refund deposit");

        paymentManager.checkAlreadyRefund(deposit);
        PaymentRefundResult refund = paymentService.refund(deposit);

        idempotentKeyRepository.save(idempotent);
        deposit.setDepositStatus(DepositStatus.REFUND);
        deposit.setRefundedDate(refund.refundedDate());
    }

    public void cancel(PaymentPrepareDto request, PaymentType type) {
        var paymentService = paymentManager.getPaymentService(type);

//        paymentService.cancel(request);
    }

    public void handlePaymentCancel(IdempotentDto idempotentDto, String type, String paymentRequestId) {
        Deposit deposit = depositQueryService.getDepositByRequestId(paymentRequestId);
        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), "already canceled payment process");

        deposit.setPaymentStatus(PaymentStatus.CANCEL_PROCESS);
        idempotentKeyRepository.save(idempotent);
    }

    public void handlePaymentFailure(IdempotentDto idempotentDto, String type, String paymentRequestId) {
        Deposit deposit = depositQueryService.getDepositByRequestId(paymentRequestId);
        Idempotent idempotent = IdempotentDto.of(idempotentDto, HttpStatus.OK.value(), "already failed payment process");

        deposit.setPaymentStatus(PaymentStatus.FAIL_PROCESS);
        idempotentKeyRepository.save(idempotent);
    }

    public void postProcessing(Long id, String orderNumber) {
        Deposit deposit = depositQueryService.getDepositById(id);

        OrderInfo orderInfo = orderInfoQueryService.fetchAccount(orderNumber);

        deposit.setOrderInfo(orderInfo);
    }
}
