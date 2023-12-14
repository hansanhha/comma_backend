package know_wave.comma.arduino.component.service;

import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.arduino.component.dto.ArduinoCategoriesResponse;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.entity.ArduinoCategory;
import know_wave.comma.arduino.component.repository.ArduinoCategoryRepository;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.arduino.component.dto.ArduinoDetailResponse;
import know_wave.comma.arduino.component.dto.ArduinoPageResponse;
import know_wave.comma.arduino.component.dto.CommentPageResponse;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.repository.ArduinoCommentRepository;
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
public class ArduinoComponentService {

    private final ArduinoRepository arduinoRepository;
    private final ArduinoCategoryRepository categoryRepository;
    private final ArduinoCommentRepository commentRepository;


    public ArduinoCategoriesResponse getCategories() {
        var categories = (List<ArduinoCategory>) categoryRepository.findAll();
        return ArduinoCategoriesResponse.to(categories);
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

    public CommentPageResponse getArduinoComment(Long arduinoId, Pageable pageable) {
        Arduino arduino = getArduino(arduinoId);
        Page<Comment> comments = commentRepository.findAllByArduino(arduino, pageable);

        return CommentPageResponse.to(comments);
    }

    public CommentPageResponse getArduinoReplyComment(Long commentId, Pageable pageable) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE));

        Page<Comment> replyComments = commentRepository.findAllReplyById(parentComment, pageable);

        return CommentPageResponse.to(replyComments);
    }

    public Arduino getArduino(Long arduinoId) {
        return arduinoRepository.findById(arduinoId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }

}
