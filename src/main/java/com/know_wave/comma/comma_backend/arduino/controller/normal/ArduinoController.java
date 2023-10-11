package com.know_wave.comma.comma_backend.arduino.controller.normal;

import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoDetailResponse;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoReplyCommentResponse;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoResponse;
import com.know_wave.comma.comma_backend.arduino.dto.category.CategoryDto;
import com.know_wave.comma.comma_backend.arduino.dto.comment.CommentRequest;
import com.know_wave.comma.comma_backend.arduino.dto.comment.ReplyCommentRequest;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoCommunityService;
import com.know_wave.comma.comma_backend.arduino.service.normal.ArduinoService;
import jakarta.validation.Valid;
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
