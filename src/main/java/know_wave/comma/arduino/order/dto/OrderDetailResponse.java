package know_wave.comma.arduino.order.dto;

import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDetailResponse {

    public static OrderDetailResponse to(Order order, List<OrderDetail> orderDetails) {
        return new OrderDetailResponse(OrderResponse.to(order),
                orderDetails.stream().map(OrderDetailInfo::of).toList());
    }

    private final OrderResponse orderInfo;
    private final List<OrderDetailInfo> orderDetailInfos;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderDetailInfo {

        public static OrderDetailInfo of(OrderDetail orderDetail) {
            return new OrderDetailInfo(orderDetail.getArduino().getId(),
                    orderDetail.getArduino().getName(), orderDetail.getOrderArduinoCount());
        }

        private final Long arduinoId;
        private final String arduinoName;
        private final int orderArduinoCount;
    }

}
