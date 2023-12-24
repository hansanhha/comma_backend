package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.order.entity.Order;
import know_wave.comma.arduino.order.entity.OrderDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDetailResponse {

    public static OrderDetailResponse create(Order order, List<OrderDetail> orderDetails) {
        return new OrderDetailResponse(OrderResponse.to(order),
                orderDetails.stream().map(OrderDetailInfo::to).toList());
    }

    @JsonProperty("order_info")
    private final OrderResponse orderInfo;

    @JsonProperty("order_detail_infos")
    private final List<OrderDetailInfo> orderDetailInfos;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class OrderDetailInfo {

        public static OrderDetailInfo to(OrderDetail orderDetail) {
            return new OrderDetailInfo(orderDetail.getArduino().getId(),
                    orderDetail.getArduino().getName(), orderDetail.getOrderArduinoCount());
        }

        @JsonProperty("arduino_id")
        private final Long arduinoId;

        @JsonProperty("arduino_name")
        private final String arduinoName;

        @JsonProperty("order_arduino_count")
        private final int orderArduinoCount;
    }

}
