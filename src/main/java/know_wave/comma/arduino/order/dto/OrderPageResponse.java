package know_wave.comma.arduino.order.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPageResponse {

    public static OrderPageResponse to(List<OrderResponse> orders, boolean isFirst, boolean isLast, boolean hasNext, int size) {
        return new OrderPageResponse(orders, isFirst, isLast, hasNext, size);
    }

    private final List<OrderResponse> orders;
    private final boolean isFirst;
    private final boolean isLast;
    private final boolean hasNext;
    private final int size;

}
