package know_wave.comma.arduino.notification.service;

import jakarta.annotation.PostConstruct;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.arduino.notification.dto.OrderNotificationRequest;
import know_wave.comma.arduino.order.entity.DepositStatus;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import know_wave.comma.arduino.order.entity.OrderStatus;
import know_wave.comma.arduino.order.exception.OrderException;
import know_wave.comma.arduino.order.repository.OrderDetailRepository;
import know_wave.comma.arduino.order.repository.OrderRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.common.notification.push.dto.PushNotificationRequest;
import know_wave.comma.common.notification.push.entity.NotificationFeature;
import know_wave.comma.common.notification.push.entity.PushNotificationType;
import know_wave.comma.common.notification.push.service.PushNotificationGateway;
import know_wave.comma.common.notification.realtime.sse.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArduinoOrderNotification {

    private final PushNotificationGateway pushNotificationGateway;
    private final SseEmitterService sseEmitterService;
    private final AccountQueryService accountQueryService;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    private static final NotificationFeature ARDUINO_ORDER = NotificationFeature.ARDUINO_ORDER;
    private static final Map<OrderStatus, String> SSE_EVENT_NAME_MAP = new HashMap<>();
    private static final Map<OrderStatus, String> PUSH_NOTIFICATION_TITLE_MAP = new HashMap<>();
    private static final Map<OrderStatus, String> PUSH_NOTIFICATION_CONTENT_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        SSE_EVENT_NAME_MAP.put(OrderStatus.ORDERED, "order_success");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_SERVER, "order_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE, "order_deposit_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL, "order_deposit_cancel");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS, "order_arduino_status_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK, "order_arduino_stock_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_REJECTED, "order_rejected");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FREE_CANCEL, "order_cancel");
        SSE_EVENT_NAME_MAP.put(OrderStatus.CANCEL, "order_cancel");
        SSE_EVENT_NAME_MAP.put(OrderStatus.PREPARING, "order_prepare");
        SSE_EVENT_NAME_MAP.put(OrderStatus.BE_READY, "order_ready");
        SSE_EVENT_NAME_MAP.put(OrderStatus.RECEIVE_DEPOSIT_REFUND_SUCCESS, "order_receive");
        SSE_EVENT_NAME_MAP.put(OrderStatus.RECEIVE_DEPOSIT_REFUND_FAILURE, "order_receive_deposit_refund_failure");

        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.ORDERED, "컴마 실습재료 부품 주문 완료 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_SERVER, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_REJECTED, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FREE_CANCEL, "컴마 실습재료 부품 주문 취소 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.CANCEL, "컴마 실습재료 부품 주문 취소 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.PREPARING, "컴마 실습재료 부품 주문 준비 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.BE_READY, "컴마 실습재료 부품 주문 준비완료 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.RECEIVE_DEPOSIT_REFUND_SUCCESS, "컴마 실습재료 부품 주문수령 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.RECEIVE_DEPOSIT_REFUND_FAILURE, "컴마 실습재료 부품 주문수령 안내");

        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.ORDERED,
                """
                <br>    
                주문이 완료되었습니다<br>
                주문 상태가 변경되면 알림을 통해 안내드립니다<br>
                주문 조회 페이지에서 직접 확인하실 수도 있습니다<br>
                <br>
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_SERVER,
                """
                <br>
                서버 오류로 인해 주문이 실패되었습니다<br>
                다시 시도해주시기 바랍니다<br>
                <br>
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE,
                """
                <br>
                보증금 결제 실패로 인해 주문이 실패되었습니다<br>
                <br>
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL,
                """
                <br>    
                보증금 결제 취소로 인해 주문이 실패되었습니다<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS,
                """
                <br>    
                부품 재고 상태로 인해 주문이 실패되었습니다<br>
                * 보증금을 결제하셨다면 메일 전송일 기준 2일 이내에 반환됩니다<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK,
                """
                <br>   
                부품의 잔여 수량이 부족하여 주문이 실패되었습니다<br>
                * 보증금을 결제하셨다면 메일 전송일 기준 2일 이내에 반환됩니다<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_REJECTED,
                """
                <br>
                관리자에 의해 주문이 거부되었습니다<br>
                자세한 사항은 관리자톡을 통해 문의해주세요<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.CANCEL,
                """
                <br>    
                주문 취소가 완료되었습니다<br>
                주문 준비, 준비 완료 상태인 경우에는 보증금 반환이 불가합니다<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FREE_CANCEL,
                """
                <br>    
                주문 취소와 보증금 환불이 완료되었습니다<br>
                주문이 접수되기 이전에는 보증금 반환이 가능합니다<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.PREPARING,
                """
                <br>    
                주문이 접수되어 부품을 준비 중입니다<br>
                준비 완료 시 알림을 통해 안내드리겠습니다<br>
                * 주문 취소 시 보증금 반환이 불가한 점을 참고해주세요<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.BE_READY,
                """
                <br>
                부품이 준비되었습니다<br>
                
                학과 사무실(3호관 210호)에 방문하여 부품을 수령해주세요<br>
                운영시간: 09시 ~ 18시 (점심시간: 12시~13시)<br>
                * 주문 취소 시 보증금 반환이 불가한 점을 참고해주세요<br>
                <br>
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.RECEIVE_DEPOSIT_REFUND_SUCCESS,
                """
                <br>    
                부품 수령과 보증금 반환이 완료되었습니다<br>
                컴마를 이용해주셔서 감사합니다<br>
                <br>    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.RECEIVE_DEPOSIT_REFUND_FAILURE,
                """
                <br>    
                부품 수령이 완료되었지만 보증금 반환 처리에서 문제가 발생하였습니다<br>
                매끄럽게 처리되지 않아 죄송합니다<br>
                보증금은 메일 전송일 기준 2일 이내에 반환됩니다<br>
                <br>    
                """);

    }

    public void notify(OrderNotificationRequest request) {
        String accountId = request.getAccountId();
        Account account = accountQueryService.findAccount(accountId);

        OrderStatus orderStatus = request.getOrderStatus();
        DepositStatus depositStatus = request.getDepositStatus();
        String orderNumber = request.getOrderNumber();

        var destMap = Map.of(PushNotificationType.EMAIL, account.getEmail());

        if (sseEmitterService.isConnected(accountId)) {
            sseEmitterService.send(accountId, SSE_EVENT_NAME_MAP.get(orderStatus), orderNumber);
        }

        PushNotificationRequest pushNotificationRequest =
                PushNotificationRequest.create(accountId, destMap, PUSH_NOTIFICATION_TITLE_MAP.get(orderStatus),
                        getContent(orderStatus, depositStatus, orderNumber), ARDUINO_ORDER, null);

        pushNotificationGateway.notify(pushNotificationRequest);
    }

    private String getContent(OrderStatus orderStatus, DepositStatus depositStatus, String orderNumber) {
        Order order = orderRepository.findFetchByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER));

        List<OrderDetail> orderDetails = orderDetailRepository.findFetchAllByOrder(order);

        String orderDetailsStr = orderDetails.stream()
                .map(orderDetail -> orderDetail.getArduino().getName() + "&nbsp;&nbsp;" + orderDetail.getOrderArduinoCount() + "개")
                .collect(Collectors.joining("<br>"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = order.getCreatedDate().format(formatter);

        return """
        %s<br>
        <strong>주문정보</strong><br>
        주문번호: %s<br>
        주문상태: %s<br>
        보증금 상태: %s<br>
        과목: %s<br>
        주문일자: %s<br>
        <br>
        <strong>주문내역</strong><br>
        %s
        """.formatted(PUSH_NOTIFICATION_CONTENT_MAP.get(orderStatus), orderNumber, orderStatus.getStatus(), depositStatus.getStatus(),
                order.getSubject().getSubjectName(), formattedDate, orderDetailsStr);
    }


}
