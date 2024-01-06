package know_wave.comma.common.entity;

import org.springframework.stereotype.Component;

@Component
public class ExceptionMessageSource {

    public static final String SIGN_IN_REQUIRED = "Sign in required";
    public static final String UNABLE_SEND_EMAIL = "Unable to send email";
    public static final String ALREADY_VERIFIED_EMAIL = "Already verified email";
    public static final String NOT_VERIFIED_EMAIL = "Not verified email";
    public static final String NOT_FOUND_EMAIL = "not found email";
    public static final String NOT_EXIST_ACCOUNT = "Not exist account";
    public static final String NOT_FOUND_TOKEN = "not found token";
    public static final String TEMPERED_TOKEN = "Tempered token";
    public static final String INVALID_TOKEN = "invalid token";
    public static final String ALREADY_EXIST_VALUE = "Already exist value";
    public static final String PERMISSION_DENIED = "permission denied";
    public static final String NOT_FOUND_VALUE = "Not found value";
    public static final String NOT_SUPPORTED_ARGUMENT = "Not supported argument";
    public static final String ALREADY_IN_BASKET = "Already in basket";
    public static final String INVALID_VALUE = "Invalid value";
    public static final String NOT_SUPPORTED_PAYMENT_TYPE = "not supported payment type";
    public static final String ALREADY_PAID = "Already paid";
    public static final String ALREADY_REFUNDED = "Already refunded";
    public static final String SSE_EMITTER_SEND_ERROR = "SSE Emitter send error";
    public static final String SSE_EMITTER_ALREADY_EXIST = "already connect sse emitter";
    public static final String SSE_EMITTER_NOT_EXIST = "SseEmitter not exist";
    public static final String BLOCKED_ACCOUNT = "blocked account";
    public static final String DELETED_ACCOUNT = "deleted account";
    public static final String INACTIVE_ACCOUNT = "inactive account";
    public static final String NOT_WRITER = "not writer";
    public static final String NOT_FOUND_COMMENT = "not found comment";
    public static final String OVER_MAX_ARDUINO_QUANTITY = "over max arduino quantity";
    public static final String NOT_ENOUGH_ARDUINO_STOCK = "not enough arduino stock";
    public static final String BAD_ARDUINO_STOCK_STATUS = "bad arduino stock status";
    public static final String EMPTY_BASKET = "empty basket";
    public static final String IDEMPOTENCY_UNPROCESSABLE_EXCEPTION = "unprocessible idempontent";
    public static final String INVALID_IDEMPOTENT_RESPONSE = "invalid idempotent response";
    public static final String NOT_FOUND_PAYMENT_INFO = "not found payment info";
    public static final String NOT_SUPPORTED_PAYMENT_FEATURE = "not supported payment feature";
    public static final String INVALID_ORDER_NUMBER = "invalid order number";
    public static final String UNABLE_TO_BASKET = "장바구니에 담을 수 없습니다";
    public static final String UNABLE_TO_ORDER = "주문을 수행할 수 없습니다";
    public static final String UNABLE_TO_PREPARE_ORDER = "주문 준비를 수행할 수 없습니다";
    public static final String UNABLE_TO_REJECT_ORDER = "주문 거부를 수행할 수 없습니다";
    public static final String UNABLE_TO_CANCEL_ORDER = "주문 취소를 수행할 수 없습니다";
    public static final String UNABLE_TO_FAIL_ORDER = "주문 실패를 수행할 수 없습니다";
    public static final String UNABLE_TO_BE_READY_ORDER = "주문 준비 완료를 수행할 수 없습니다";
    public static final String UNABLE_TO_RECEIVE_ORDER = "주문 수령을 수행할 수 없습니다";
    public static final String FAILED_DEPOSIT_RETURN = "보증금 반환에 실패했습니다";
    public static final String NOT_FOUND_ROLE = "권한을 찾을 수 없습니다";
    public static final String EXPIRED_TOKEN = "토큰이 만료되었습니다";
    public static final String FAIL_SIGN_IN = "아이디 또는 비밀번호가 올바르지 않습니다";
    public static final String INVALID_ORDER_STATUS = "유효하지 않은 주문 상태입니다";
    public static final String INVALID_PAYMENT_CHECKOUT_REQUEST = "유효하지 않은 결제 요청입니다";
    public static final String PAYMENT_SERVER_ERROR = "결제 서버 오류가 발생했습니다";
    public static final String RETRY_PAYMENT_CHECKOUT_REQUEST = "결제 요청을 실패했습니다. 잠시 후 다시 시도해주세요";
}
