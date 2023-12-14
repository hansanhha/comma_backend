package know_wave.comma.common.entity;

import org.springframework.stereotype.Component;

@Component
public class ExceptionMessageSource {

    public static final String SIGN_IN_REQUIRED = "Sign in required";
    public static final String UNABLE_SEND_EMAIL = "Unable to send email";
    public static final String ALREADY_VERIFIED_EMAIL = "Already verified email";
    public static final String NOT_VERIFIED_EMAIL = "Not verified email";
    public static final String NOT_FOUND_EMAIL = "Not found email";
    public static final String BAD_CREDENTIALS = "Bad credentials";
    public static final String NOT_EXIST_ACCOUNT = "Not exist account";
    public static final String NOT_EXIST_CATEGORY = "Not exist category";
    public static final String NOT_FOUND_TOKEN = "Not found token";
    public static final String TEMPERED_TOKEN = "Tempered token";
    public static final String NOT_FOUND_ACCESS_TOKEN = "Not found access token";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String EXPIRED_REFRESH_TOKEN = "Expired refresh token. login is required";
    public static final String EXPIRED_ACCESS_TOKEN = "Expired access token. re-issuance is required";
    public static final String NOT_FOUND_ARDUINO = "Not found arduino";
    public static final String NOT_FOUND_COMMENT = "Not found comment";
    public static final String ALREADY_EXIST_ARDUINO = "Already exist arduino";
    public static final String ALREADY_EXIST_VALUE = "Already exist value";
    public static final String PERMISSION_DENIED = "Permission denied";
    public static final String NOT_FOUND_VALUE = "Not found value";
    public static final String NOT_SUPPORTED_ARGUMENT = "Not supported argument";
    public static final String ALREADY_IN_BASKET = "Already in basket";
    public static final String NOT_ACCEPTABLE_REQUEST = "Not acceptable request";
    public static final String INVALID_VALUE = "Invalid value";
    public static final String NOT_SUPPORTED_PAYMENT_TYPE = "지원하지 않는 결제 수단입니다";
    public static final String ALREADY_PAID = "Already paid";
    public static final String ALREADY_REFUNDED = "Already refunded";
    public static final String SSE_EMITTER_SEND_ERROR = "SSE Emitter 전송 오류";
    public static final String SSE_EMITTER_ALREADY_EXIST = "연결되어 있는 SseEmitter가 이미 존재합니다";
    public static final String SSE_EMITTER_NOT_EXIST = "SseEmitter not exist";
    public static final String BLOCKED_ACCOUNT = "정지된 계정입니다";
    public static final String DELETED_ACCOUNT = "탈퇴된 계정입니다";
    public static final String INACTIVE_ACCOUNT = "비활성화된 계정입니다";
    public static final String NOT_WRITER = "작성자가 아닙니다";
    public static final String NOT_EXIST_COMMENT = "존재하지 않는 댓글입니다";
    public static final String OVER_MAX_ARDUINO_QUANTITY = "최대 신청 가능 수량을 초과했습니다";
    public static final String NOT_ENOUGH_ARDUINO_STOCK = "부품 잔여 수량을 초과했습니다";
    public static final String BAD_ARDUINO_STOCK_STATUS = "부품 재고 상태가 유효하지 않습니다";
    public static final String EMPTY_BASKET = "장바구니가 비어있습니다";
    public static final String IDEMPOTENCY_UNPROCESSABLE_EXCEPTION = "멱등하지 않은 요청입니다";
    public static final String INVALID_IDEMPOTENT_RESPONSE = "잘못된 형식의 멱등 응답입니다";
    public static final String NOT_EXIST_PAYMENT_INFO = "결제 정보가 존재하지 않습니다";
    public static final String NOT_SUPPORTED_PAYMENT_FEATURE = "지원하지 않는 결제 기능입니다";
    public static final String INVALID_ORDER_NUMBER = "유효하지 않은 주문번호입니다";
    public static final String UNABLE_TO_BASKET = "장바구니에 담을 수 없습니다";
    public static final String UNABLE_TO_ORDER = "주문을 수행할 수 없습니다";
    public static final String UNABLE_TO_PREPARE_ORDER = "주문 준비를 수행할 수 없습니다";
    public static final String UNABLE_TO_REJECT_ORDER = "주문 거부를 수행할 수 없습니다";
    public static final String UNABLE_TO_CANCEL_ORDER = "주문 취소를 수행할 수 없습니다";
    public static final String UNABLE_TO_FAIL_ORDER = "주문 실패를 수행할 수 없습니다";
    public static final String UNABLE_TO_BE_READY_ORDER = "주문 준비 완료를 수행할 수 없습니다";
    public static final String UNABLE_TO_RECEIVE_ORDER = "주문 수령을 수행할 수 없습니다";
    public static final String FAILED_DEPOSIT_RETURN = "보증금 반환에 실패했습니다";
}
