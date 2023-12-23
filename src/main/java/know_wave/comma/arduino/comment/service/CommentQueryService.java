package know_wave.comma.arduino.comment.service;

import know_wave.comma.arduino.comment.dto.CommentPageResponse;
import know_wave.comma.arduino.comment.dto.ReplyCommentPageResponse;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.exception.NotFoundArduinoException;
import know_wave.comma.arduino.comment.repository.CommentLikeRepository;
import know_wave.comma.arduino.comment.repository.CommentRepository;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.repository.ArduinoRepository;
import know_wave.comma.common.entity.ExceptionMessageSource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentQueryService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArduinoRepository arduinoRepository;

    public CommentPageResponse getComments(Long arduinoId, Pageable pageable) {
        Arduino arduino = getArduino(arduinoId);
        Page<Comment> comments = commentRepository.findAllByArduino(arduino, pageable);

        return CommentPageResponse.to(comments);
    }

    public ReplyCommentPageResponse getReplyComments(Long commentId, Pageable pageable) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE));

        Page<Comment> replyComments = commentRepository.findAllReplyById(parentComment, pageable);

        return ReplyCommentPageResponse.to(replyComments);
    }

    private Arduino getArduino(Long arduinoId) {
        return arduinoRepository.findById(arduinoId)
                .orElseThrow(() -> new NotFoundArduinoException(ExceptionMessageSource.NOT_FOUND_VALUE));
    }
}
