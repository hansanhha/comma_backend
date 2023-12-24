package know_wave.comma.arduino.component.admin.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.component.admin.dto.ArduinoCreateForm;
import know_wave.comma.arduino.component.admin.dto.ArduinoCreateForms;
import know_wave.comma.arduino.component.admin.dto.ArduinoUpdateForm;
import know_wave.comma.arduino.component.admin.service.ComponentCommandAdminService;
import know_wave.comma.arduino.component.admin.service.ComponentQueryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ComponentAdminController {

    private final ComponentQueryAdminService componentQueryAdminService;
    private final ComponentCommandAdminService componentCommandAdminService;

    private static final String MESSAGE = "message";
    private static final String DATA = "body";

    @PostMapping("/arduino")
    public ResponseEntity<Map<String, String>> registerArduino(@Valid @RequestBody ArduinoCreateForm form) {
        componentCommandAdminService.registerArduino(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE, "registered arduino"));
    }

    @PostMapping("/arduinos")
    public ResponseEntity<Map<String, String>> registerArduinos(@Valid @RequestBody ArduinoCreateForms form) {
        componentCommandAdminService.registerArduinoList(form.getArduinoCreateForms());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE, "registered arduinos"));
    }

    @PutMapping("/arduino/{id}")
    public Map<String, String> updateArduino(@PathVariable Long id, @Valid @RequestBody ArduinoUpdateForm form) {
        componentCommandAdminService.updateArduino(id, form);
        return Map.of(MESSAGE, "updated arduino");
    }

    @DeleteMapping("/arduino/{id}")
    public Map<String, String> deleteArduino(@PathVariable Long id) {
        componentCommandAdminService.deleteArduino(id);
        return Map.of(MESSAGE, "deleted arduino");
    }

}
