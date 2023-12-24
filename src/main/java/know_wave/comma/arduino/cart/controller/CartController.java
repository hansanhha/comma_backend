package know_wave.comma.arduino.cart.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.cart.dto.CartAddRequest;
import know_wave.comma.arduino.cart.dto.CartUpdateRequest;
import know_wave.comma.arduino.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private static final String MESSAGE = "message";
    private static final String DATA = "body";

    @GetMapping("/cart")
    public Map<String, Object> getBasket() {
        return Map.of(MESSAGE, "basket", DATA, cartService.getBasket());
    }

    @PostMapping("/cart/arduinos/{arduinoId}")
    public Map<String, String> addArduino(@PathVariable Long arduinoId, @Valid @RequestBody CartAddRequest addRequest) {
        cartService.addArduino(arduinoId, addRequest);
        return Map.of(MESSAGE, "added arduino in basket");
    }

    @PatchMapping("/cart/{cartId}")
    public Map<String, String> updateArduino(@PathVariable Long cartId,
                                             @Valid @RequestBody CartUpdateRequest updateRequest) {
        cartService.updateContainCount(cartId, updateRequest);
        return Map.of(MESSAGE, "updated arduino in basket");
    }

    @DeleteMapping("/cart/{cartId}")
    public Map<String, String> deleteArduino(@PathVariable Long cartId) {
        cartService.deleteArduino(cartId);
        return Map.of(MESSAGE, "deleted arduino in basket");
    }

    @DeleteMapping("/carts")
    public Map<String, String> deleteAllArduino() {
        cartService.deleteAllArduino();
        return Map.of(MESSAGE, "empty basket");
    }

}
