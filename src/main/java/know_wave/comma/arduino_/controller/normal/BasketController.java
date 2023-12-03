package know_wave.comma.arduino_.controller.normal;

import know_wave.comma.arduino_.dto.basket.BasketDeleteRequest;
import know_wave.comma.arduino_.dto.basket.BasketRequest;
import know_wave.comma.arduino_.dto.basket.BasketResponse;
import know_wave.comma.arduino_.service.normal.BasketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping
    public List<BasketResponse> getBasket() {
        return basketService.getBasket();
    }

    @PostMapping("/arduino")
    public String addArduinoToBasket(@Valid @RequestBody BasketRequest request) {
        basketService.addArduinoToBasket(request);
        return "Added arduino to basket";
    }

    @DeleteMapping("/arduino")
    public String deleteArduinoFromBasket(@Valid @RequestBody BasketDeleteRequest request) {
        basketService.deleteArduinoFromBasket(request);
        return "Deleted arduino from basket";
    }

    @PatchMapping("/arduino")
    public String updateArduinoFromBasket(@Valid @RequestBody BasketRequest request) {
        basketService.updateArduinoFromBasket(request);
        return "Updated arduino from basket";
    }

    @DeleteMapping
    public String emptyBasket() {
        basketService.emptyBasket();
        return "Deleted all arduino from basket";
    }
}
