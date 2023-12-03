package know_wave.comma.arduino_.dto.basket;

import know_wave.comma.arduino_.dto.arduino.ArduinoResponse;
import know_wave.comma.arduino_.entity.Basket;

import java.util.List;

public class BasketResponse {

    public static List<BasketResponse> of(List<Basket> arduinoInFromBasket) {

        return arduinoInFromBasket
                .stream()
                .map(basket -> new BasketResponse(ArduinoResponse.ofWithoutLike(basket.getArduino()), basket.getStoredArduinoCount()))
                .toList();
    }

    private BasketResponse(ArduinoResponse arduino, int containedCount) {
        this.arduino = arduino;
        ContainedCount = containedCount;
    }

    private final ArduinoResponse arduino;
    private final int ContainedCount;


    public ArduinoResponse getArduino() {
        return arduino;
    }

    public int getContainedCount() {
        return ContainedCount;
    }
}
