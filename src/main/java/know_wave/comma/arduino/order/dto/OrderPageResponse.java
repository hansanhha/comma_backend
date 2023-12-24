package know_wave.comma.arduino.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderPageResponse {

    public static OrderPageResponse create(List<OrderResponse> orders, boolean isFirst, boolean isLast, boolean hasNext, int size) {
        return new OrderPageResponse(orders, isFirst, isLast, hasNext, size);
    }

    @JsonProperty("orders")
    private final List<OrderResponse> orders;

    @JsonProperty("is_first")
    private final Boolean isFirst;

    @JsonProperty("is_last")
    private final Boolean isLast;

    @JsonProperty("has_next")
    private final Boolean hasNext;

    @JsonProperty("size")
    private final int size;

}
