package know_wave.comma.order_.dto;

import java.util.List;
import java.util.Map;

public class OrderResponseGroup {

    public static List<OrderResponseGroup> ofList(Map<String, List<OrderResponse>> groupedOrderResponses) {
        return  groupedOrderResponses.keySet().stream()
                .map(key -> {
                    List<OrderResponse> orderResponses = groupedOrderResponses.get(key);
                    return OrderResponseGroup.of(orderResponses);
                })
                .toList();
    }

    private static OrderResponseGroup of(List<OrderResponse> userOrderList) {
        return new OrderResponseGroup(userOrderList);
    }

    private OrderResponseGroup(List<OrderResponse> userOrderList) {
        this.userOrderList = userOrderList;
    }

    List<OrderResponse> userOrderList;

    public List<OrderResponse> getUserOrderList() {
        return userOrderList;
    }
}
