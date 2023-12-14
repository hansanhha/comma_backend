package know_wave.comma.arduino.basket.dto;

import know_wave.comma.arduino.basket.entity.Basket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BasketResponse {

    public static BasketResponse to(List<Basket> baskets) {
        return new BasketResponse(baskets.stream()
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
