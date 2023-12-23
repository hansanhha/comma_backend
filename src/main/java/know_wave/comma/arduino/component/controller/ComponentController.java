package know_wave.comma.arduino.component.controller;

import know_wave.comma.arduino.component.dto.ArduinoDetailResponse;
import know_wave.comma.arduino.component.dto.ArduinoPageResponse;
import know_wave.comma.arduino.component.dto.CategoriesResponse;
import know_wave.comma.arduino.component.service.ComponentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentQueryService componentQueryService;
    private static final String MESSAGE = "message";
    private static final String DATA = "body";

    @GetMapping("/arduino/categories")
    public Map<String, Object> getCategories() {
        CategoriesResponse categories = componentQueryService.getCategories();
        return Map.of(MESSAGE, "categories", DATA, categories);
    }

    @GetMapping("/arduinos")
    public Map<String, Object> getArduinos(Pageable pageable) {
        ArduinoPageResponse arduinoPage = componentQueryService.getArduinoPage(pageable);
        return Map.of(MESSAGE, "arduinos", DATA, arduinoPage);
    }

    @GetMapping("/arduinos/{id}")
    public Map<String, Object> getArduinoDetail(@PathVariable Long id) {
        ArduinoDetailResponse arduinoDetailWithComments = componentQueryService.getArduinoDetailWithComments(id);
        return Map.of(MESSAGE, "arduino detail", DATA, arduinoDetailWithComments);
    }

}
