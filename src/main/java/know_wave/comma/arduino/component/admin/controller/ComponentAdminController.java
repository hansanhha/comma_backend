package know_wave.comma.arduino.component.admin.controller;

import know_wave.comma.arduino.component.admin.dto.ArduinoCreateForm;
import know_wave.comma.arduino.component.admin.dto.ArduinoUpdateForm;
import know_wave.comma.arduino.component.admin.service.ComponentCommandAdminService;
import know_wave.comma.arduino.component.admin.service.ComponentQueryAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(path ="/arduino", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> createArduino(@ModelAttribute ArduinoCreateForm form,
                                                             @RequestPart(name = "photo_files", required = false) List<MultipartFile> photoFiles) {
        componentCommandAdminService.createArduino(form, photoFiles);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE, "registered arduino"));
    }

    @PostMapping("/arduinos")
    public ResponseEntity<Map<String, String>> createArduinos(@ModelAttribute("arduinos") List<ArduinoCreateForm> forms) {
        componentCommandAdminService.createArduinoList(forms);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(MESSAGE, "registered arduinos"));
    }

    @PutMapping(path = "/arduino/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> updateArduino(@PathVariable Long id,
                                             @ModelAttribute ArduinoUpdateForm form,
                                             @RequestPart(name = "delete_photo_files", required = false) List<String> deletePhotoFiles,
                                             @RequestPart(name = "add_photo_files", required = false) List<MultipartFile> addPhotoFiles) {
        componentCommandAdminService.updateArduino(id, form, deletePhotoFiles, addPhotoFiles);
        return Map.of(MESSAGE, "updated arduino");
    }

    @DeleteMapping("/arduino/{id}")
    public Map<String, String> deleteArduino(@PathVariable Long id) {
        componentCommandAdminService.deleteArduino(id);
        return Map.of(MESSAGE, "deleted arduino");
    }

}
