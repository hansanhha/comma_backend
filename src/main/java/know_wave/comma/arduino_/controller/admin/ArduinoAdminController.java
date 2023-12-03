package know_wave.comma.arduino_.controller.admin;

import know_wave.comma.arduino_.dto.arduino.ArduinoCreateForm;
import know_wave.comma.arduino_.dto.arduino.ArduinoCreateFormList;
import know_wave.comma.arduino_.dto.arduino.ArduinoUpdateRequest;
import know_wave.comma.arduino_.dto.category.CategoryNameDto;
import know_wave.comma.arduino_.service.admin.ArduinoAdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class ArduinoAdminController {

    private final ArduinoAdminService adminService;

    public ArduinoAdminController(ArduinoAdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/arduinos/category")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryNameDto request) {
        adminService.registerCategory(request.getCategoryName().trim());
        return new ResponseEntity<>("Created category", HttpStatus.CREATED);
    }

    @PatchMapping("/arduinos/categories/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryNameDto request) {
        adminService.updateCategory(id, request.getCategoryName());
        return new ResponseEntity<>("Updated category", HttpStatus.OK);
    }

    @DeleteMapping("/arduinos/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long id) {
        adminService.deleteCategory(id);
        return new ResponseEntity<>("Deleted category", HttpStatus.OK);
    }

    @PostMapping("/arduino")
    public ResponseEntity<String> addArduino(@Valid @RequestBody ArduinoCreateForm form) {
        adminService.registerArduino(form);
        return new ResponseEntity<>("Created arduino", HttpStatus.CREATED);
    }

    @PostMapping("/arduinos")
    public ResponseEntity<String> addArduinoList(@Valid @RequestBody ArduinoCreateFormList forms) {
        adminService.registerArduinoList(forms);
        return new ResponseEntity<>("Created arduino list", HttpStatus.CREATED);
    }

    @PutMapping("/arduinos/{id}")
    public ResponseEntity<String> updateArduino(@PathVariable("id") Long id, @Valid @RequestBody ArduinoUpdateRequest form) {
        adminService.updateArduino(id, form);
        return new ResponseEntity<>("Updated arduino", HttpStatus.OK);
    }

    @DeleteMapping("/arduinos/{id}")
    public ResponseEntity<String> deleteArduino(@PathVariable("id") Long id) {
        adminService.deleteArduino(id);
        return new ResponseEntity<>("Deleted arduino", HttpStatus.OK);
    }
}
