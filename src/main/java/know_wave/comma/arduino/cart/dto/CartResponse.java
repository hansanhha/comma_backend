package know_wave.comma.arduino.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import know_wave.comma.arduino.cart.entity.Cart;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CartResponse {

    public static CartResponse to(List<Cart> carts) {
        return new CartResponse(carts.stream()
                .map(basket -> new StoredArduino(basket.getId(), basket.getArduino().getId(), basket.getStoredCount()))
                .toList());
    }

    private final List<StoredArduino> carts;

    @Getter
    @RequiredArgsConstructor
    private static class StoredArduino {
        @JsonProperty("cart_id")
        private final Long cartId;

        @JsonProperty("arduino_id")
        private final Long arduinoId;

        @JsonProperty("count")
        private final int containCount;
    }
}
