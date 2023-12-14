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
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE, "order_deposit_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL, "order_deposit_cancel");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS, "order_arduino_status_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK, "order_arduino_stock_failure");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FAILURE_CAUSE_REJECTED, "order_rejected");
        SSE_EVENT_NAME_MAP.put(OrderStatus.FREE_CANCEL, "order_cancel");
        SSE_EVENT_NAME_MAP.put(OrderStatus.CANCEL, "order_cancel");
        SSE_EVENT_NAME_MAP.put(OrderStatus.PREPARING, "order_prepare");
        SSE_EVENT_NAME_MAP.put(OrderStatus.BE_READY, "order_ready");
        SSE_EVENT_NAME_MAP.put(OrderStatus.RECEIVE, "order_receive");
        
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.ORDERED, "컴마 실습재료 부품 주문 완료 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FAILURE_CAUSE_REJECTED, "컴마 실습재료 부품 주문 실패 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.FREE_CANCEL, "컴마 실습재료 부품 주문 취소 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.CANCEL, "컴마 실습재료 부품 주문 취소 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.PREPARING, "컴마 실습재료 부품 주문 준비 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.BE_READY, "컴마 실습재료 부품 주문 준비완료 안내");
        PUSH_NOTIFICATION_TITLE_MAP.put(OrderStatus.RECEIVE, "컴마 실습재료 부품 주문수령 안내");

        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.ORDERED,
                """
                    
                    주문이 완료되었습니다
                    주문 상태가 변경되면 알림을 통해 안내드리겠습니다
                    주문 조회 페이지에서 직접 확인하실 수도 있습니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_FAILURE,
                """
                    
                    보증금 결제 실패로 인해 주문이 실패되었습니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_DEPOSIT_CANCEL,
                """
                    
                    보증금 결제 취소로 인해 주문이 실패되었습니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK_STATUS,
                """
                    
                    주문할 수 없는 부품 재고 상태로 인해 주문이 실패되었습니다
                    * 보증금을 결제하셨다면 메일 전송일 기준 2일 이내에 반환됩니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_ARDUINO_STOCK,
                """
                    
                    부품의 잔여 수량이 부족하여 주문이 실패되었습니다
                    * 보증금을 결제하셨다면 메일 전송일 기준 2일 이내에 반환됩니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FAILURE_CAUSE_REJECTED,
                """
                    
                    관리자에 의해 주문이 거부되었습니다
                    자세한 사항은 관리자톡을 통해 문의해주세요
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.CANCEL,
                """
                    
                    주문 취소가 완료되었습니다
                    주문 준비, 준비 완료 상태인 경우에는 보증금 반환이 불가합니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.FREE_CANCEL,
                """
                    
                    주문 취소와 보증금 환불이 완료되었습니다
                    주문이 접수되기 이전에는 보증금 반환이 가능합니다
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.PREPARING,
                """
                    
                    주문이 접수되어 부품을 준비 중입니다 !!
                    준비 완료 시 알림을 통해 안내드리겠습니다
                    * 주문 취소 시 보증금 반환이 불가한 점을 참고해주세요
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.BE_READY,
                """
            
                    부품이 준비되었습니다
                    학과 사무실(3호관 210호)에 방문하여 부품을 수령해주세요.
                    운영시간: 09시 ~ 18시 (점심시간: 12시~13시)
                    * 주문 취소 시 보증금 반환이 불가한 점을 참고해주세요
                    
                """);
        PUSH_NOTIFICATION_CONTENT_MAP.put(OrderStatus.RECEIVE,
                """
                    
                    부품 수령과 보증금 반환이 완료되었습니다
                    컴마를 이용해주셔서 감사합니다
                    
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
                PushNotificationRequest.to(destMap, PUSH_NOTIFICATION_TITLE_MAP.get(orderStatus),
                        getContent(orderStatus, depositStatus, orderNumber), ARDUINO_ORDER, null);

        pushNotificationGateway.notify(pushNotificationRequest);
    }

    private String getContent(OrderStatus orderStatus, DepositStatus depositStatus, String orderNumber) {
        Order order = orderRepository.findFetchByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderException(ExceptionMessageSource.INVALID_ORDER_NUMBER));

        List<OrderDetail> orderDetails = orderDetailRepository.findFetchAllByOrder(order);

        String orderDetailsStr = orderDetails.stream()
                .map(orderDetail -> "부품명: " + orderDetail.getArduino().getName() + "\t" + orderDetail.getOrderArduinoCount() + "개")
                .collect(Collectors.joining("\n"));

        return """
        %s
        주문번호: %s
        주문상태: %s
        보증금 상태: %s
        과목: %s
        주문일자: %s
        
        주문내역
        %s
        """.formatted(PUSH_NOTIFICATION_CONTENT_MAP.get(orderStatus), orderNumber, orderStatus.getStatus(), depositStatus.getStatus(),
                order.getSubject().getSubjectName(), order.getCreatedDate(), orderDetailsStr);
    }


}
