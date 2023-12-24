package know_wave.comma.arduino.component.admin.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.component.dto.CategoriesResponse;
import know_wave.comma.arduino.component.admin.dto.CategoryRegisterRequest;
import know_wave.comma.arduino.component.admin.dto.CategoryUpdateRequest;
import know_wave.comma.arduino.component.admin.service.CategoryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/arduino")
@RequiredArgsConstructor
public class CategoryAdminController {

    private final CategoryAdminService componentAdminService;
    private static final String MESSAGE = "message";
    private static final String DATA = "body";

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        CategoriesResponse categories = componentAdminService.getCategories();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(MESSAGE, "get categories", DATA, categories));
    }

    @PostMapping("/category")
    public Map<String, String> registerCategory(@Valid @RequestBody CategoryRegisterRequest request) {
        componentAdminService.registerCategory(request.getCategoryName());
        return Map.of(MESSAGE, "registered category", DATA, request.getCategoryName());
    }

    @PatchMapping("/categories/{id}")
    public Map<String, String> updateCategory(@PathVariable Long id,
                                              @Valid @RequestBody CategoryUpdateRequest request) {
        componentAdminService.updateCategory(id, request.getCategoryName());
        return Map.of(MESSAGE, "updated category", DATA, request.getCategoryName());
    }

    @DeleteMapping("/categories/{id}")
    public Map<String, String> deleteCategory(@PathVariable Long id) {
        componentAdminService.deleteCategory(id);
        return Map.of(MESSAGE, "deleted category");
    }

}
