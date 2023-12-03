package know_wave.comma.arduino_.controller.normal;

import know_wave.comma.arduino_.dto.arduino.ArduinoDetailResponse;
import know_wave.comma.arduino_.dto.arduino.ArduinoResponse;
import know_wave.comma.arduino_.dto.category.CategoryDto;
import know_wave.comma.arduino_.service.normal.ArduinoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arduinos")
public class ArduinoController {

    private final ArduinoService arduinoService;

    @Value("${spring.data.web.pageable.default-page-size}")
    private int defaultPageSize;

    public ArduinoController(ArduinoService arduinoService) {
        this.arduinoService = arduinoService;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return arduinoService.getAllCategories();
    }

    @GetMapping("/{arduinoId}")
    public ArduinoDetailResponse getArduino(@PathVariable("arduinoId") Long id) {
        return arduinoService.getArduinoDetails(id);
    }

    @GetMapping("/{arduinoId}/comments")
    public ArduinoDetailResponse getArduinoComments(@PathVariable("arduinoId") Long id, @PageableDefault(size = 10) Pageable pageable) {
        return arduinoService.getArduinoDetails(id, pageable);
    }

    @GetMapping
    public ResponseEntity<Page<ArduinoResponse>> getArduinoListPaging(Pageable pageable) {
        var result = arduinoService.getPage(PageRequest.of(pageable.getPageNumber(), defaultPageSize, pageable.getSort()));

        return ResponseEntity.ok(result);
    }



}
