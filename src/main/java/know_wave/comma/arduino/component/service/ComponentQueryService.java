package know_wave.comma.arduino.component.service;

import know_wave.comma.arduino.comment.dto.CommentPageResponse;
import know_wave.comma.arduino.component.dto.*;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.Category;
import know_wave.comma.arduino.component.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ComponentQueryService {

    private final ArduinoRepository arduinoRepository;
    private final ArduinoCategoryRepository categoryRepository;
    private final CommentRepository commentRepository;


    public CategoriesResponse getCategories() {
        var categories = (List<Category>) categoryRepository.findAll();
        return CategoriesResponse.to(categories);
    }

    public ArduinoPageResponse getArduinoPage(Pageable pageable) {
        Page<Arduino> arduinoList = arduinoRepository.findAll(pageable);
        return ArduinoPageResponse.to(arduinoList);
    }

    public ArduinoDetailResponse getArduinoDetailWithComments(Long arduinoId) {
        Arduino arduino = getArduino(arduinoId);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments= commentRepository.findAllByArduino(arduino, pageable);

        return ArduinoDetailResponse.to(arduino, comments);
    }

    public Arduino getArduino(Long arduinoId) {
        return arduinoRepository.findById(arduinoId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }

}
