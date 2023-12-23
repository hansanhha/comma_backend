package know_wave.comma.arduino.basket.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.basket.dto.BasketAddRequest;
import know_wave.comma.arduino.basket.dto.BasketResponse;
import know_wave.comma.arduino.basket.dto.BasketUpdateRequest;
import know_wave.comma.arduino.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/arduino")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketCommandService;
    private static final String MESSAGE = "message";
    private static final String DATA = "body";

    @GetMapping("/basket")
    public Map<String, Object> getBasket() {
        return Map.of(MESSAGE, "basket", DATA, basketCommandService.getBasket());
    }

    @PostMapping("/basket")
    public Map<String, String> addArduino(@Valid @RequestBody BasketAddRequest addRequest) {
        basketCommandService.addArduino(addRequest);
        return Map.of(MESSAGE, "added arduino in basket");
    }

    @PatchMapping("/basket")
    public Map<String, String> updateArduino(@Valid @RequestBody BasketUpdateRequest updateRequest) {
        basketCommandService.updateContainCount(updateRequest);
        return Map.of(MESSAGE, "updated arduino in basket");
    }

    @DeleteMapping("/baskets/{basketId}")
    public Map<String, String> deleteArduino(@PathVariable Long basketId) {
        basketCommandService.deleteArduino(basketId);
        return Map.of(MESSAGE, "deleted arduino in basket");
    }

    @DeleteMapping("/baskets")
    public Map<String, String> deleteAllArduino() {
        basketCommandService.deleteAllArduino();
        return Map.of(MESSAGE, "empty basket");
    }



}
