package know_wave.comma.order.service.user;

import know_wave.comma.message.service.EmailSender;
import know_wave.comma.order.dto.OrderArduino;
import know_wave.comma.order.entity.Order;
import know_wave.comma.order.entity.OrderInfo;
import know_wave.comma.order.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderEmailService {

    private static final String ORDER_APPLY_CONTENT =
            """
            실습재료 신청이 완료되었습니다
            """;
    private static final String ORDER_PREPARING_CONTENT =
            """
            신청하신 실습재료를 준비 중입니다
            """;
    private static final String ORDER_CANCELLATION_REQUEST_CONTENT =
            """
            실습재료 주문 취소 요청을 관리자에게 보냈습니다. 관리자의 승인을 기다려주세요
            승인이 완료되면 알림을 보내드리겠습니다.
            주문 진행이 될 수도 있다는 점을 참고해주세요
            """;
    private static final String ORDER_REJECTED_CONTENT =
            """
            신청하신 주문 요청이 거부되었습니다
            주문을 다시 신청하시거나 관리자에게 문의해주세요
            """;
    private static final String ORDER_CANCELED_CONTENT =
            """
            신청하신 주문 취소 요청이 승인되었습니다
            """;
    private static final String ORDER_READY_CONTENT =
            """
            신청하신 주문이 준비되었습니다
            학과 사무실(컴퓨터공학관 2층 208호)에 방문하여 실습재료를 수령해주세요
            방문 가능한 시간대는 오전 8시 ~ 오후 5시입니다
            """;
    private static final String ORDER_RECEIVED_COMPLETED_CONTENT =
            """
            준비된 실습재료를 수령하셨습니다
            실습재료를 사용하시는데 불편함이 없으시길 바랍니다
            제품에 문제가 있을 경우 관리자에게 문의해주세요
            """;

    private final EmailSender emailSender;

    public void sendOrderEmail(OrderInfo orderInfo) {
        emailSender.send(
                getEmail(orderInfo),
                getTitle(orderInfo),
                getText(orderInfo)
        );
    }

    private String getEmail(OrderInfo orderInfo) {
        return orderInfo.getAccount().getEmail();
    }

    private String getTitle(OrderInfo orderInfo) {
        return orderInfo.getStatus().getNotificationTitle();
    }

    private String getText(OrderInfo orderInfo) {
        return """
                안녕하세요, %s님!
                
                %s
                주문번호: %s
                
                주문일자: %s
                
                주문내역:
                
                %s
                
                """.formatted(
                    orderInfo.getAccount().getId(),
                    selectContent(orderInfo.getStatus()),
                    orderInfo.getOrderNumber(),
                    pretty(orderInfo.getCreatedDate()),
                    getOrderHistory(orderInfo.getOrders())
                );
    }

    private String pretty(LocalDateTime createdDate) {
        return createdDate.getYear()+ "년 " +
                createdDate.getDayOfMonth() + "월 " +
                createdDate.getDayOfMonth() + "일 " +
                createdDate.getHour() + "시 " +
                createdDate.getMinute() + "분 " +
                "(" + createdDate.getDayOfWeek() + ")";
    }

    private String getOrderHistory(List<Order> orders) {
        List<OrderArduino> orderArduinos = orders.stream().map(OrderArduino::of).toList();

        return orderArduinos.stream()
                .map(orderArduino -> "- " + orderArduino.getArduinoName() + " : " + orderArduino.getOrderCount())
                .collect(Collectors.joining("\n"));
    }

    private String selectContent(OrderStatus status) {
        return switch (status) {
            case APPLIED -> ORDER_APPLY_CONTENT;
            case PREPARING -> ORDER_PREPARING_CONTENT;
            case CANCELLATION_REQUEST -> ORDER_CANCELLATION_REQUEST_CONTENT;
            case REJECTED -> ORDER_REJECTED_CONTENT;
            case CANCELED -> ORDER_CANCELED_CONTENT;
            case READY -> ORDER_READY_CONTENT;
            case RECEIVED_COMPLETED -> ORDER_RECEIVED_COMPLETED_CONTENT;
        };
    }
}
