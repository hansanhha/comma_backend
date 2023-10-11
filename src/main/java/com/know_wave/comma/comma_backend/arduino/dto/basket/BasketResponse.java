package com.know_wave.comma.comma_backend.arduino.dto.basket;

import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.entity.Basket;

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
