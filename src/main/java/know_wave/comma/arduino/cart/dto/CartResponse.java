package know_wave.comma.arduino.cart.dto;

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

    private final List<StoredArduino> baskets;

    @Getter
    @RequiredArgsConstructor
    private static class StoredArduino {
        private final Long basketId;
        private final Long arduinoId;
        private final int containCount;
    }
}
