package know_wave.comma.arduino_.controller.normal;

import know_wave.comma.arduino_.dto.arduino.ArduinoReplyCommentResponse;
import know_wave.comma.arduino_.dto.comment.CommentRequest;
import know_wave.comma.arduino_.dto.comment.ReplyCommentRequest;
import know_wave.comma.arduino_.service.normal.ArduinoCommunityService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/arduinos")
public class ArduinoCommunityController {

    private final ArduinoCommunityService arduinoCommunityService;

    public ArduinoCommunityController(ArduinoCommunityService arduinoCommunityService) {
        this.arduinoCommunityService = arduinoCommunityService;
    }

    @PostMapping("/{arduinoId}/comment")
    public String addComment(@PathVariable("arduinoId") Long id, @Valid @RequestBody CommentRequest request) {
        arduinoCommunityService.submitComment(id, request);
        return "Submitted comment";
    }

    @PatchMapping("/{arduinoId}/comments/{commentId}")
    public String updateComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                @RequestBody CommentRequest request) {
        arduinoCommunityService.updateComment(arduinoId, commentId, request);
        return "Updated comment";
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}")
    public String deleteComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId) {
        arduinoCommunityService.deleteComment(arduinoId, commentId);
        return "Deleted comment";
    }

    @GetMapping("/{arduinoId}/comments/{commentId}/reply")
    public List<ArduinoReplyCommentResponse> getReplyComment(@PathVariable("arduinoId") Long arduinoId,
                                                             @PathVariable("commentId") Long commentId,
                                                             @PageableDefault(size = 10) Pageable pageable) {
        return arduinoCommunityService.getReplyComment(arduinoId, commentId, pageable);
    }

    @PostMapping("/{arduinoId}/comments/{commentId}/reply")
    public String addReplyComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                  @Valid @RequestBody ReplyCommentRequest request) {
        arduinoCommunityService.submitReplyComment(arduinoId, commentId, request);
        return "Submitted reply comment";
    }


    @PatchMapping("/{arduinoId}/comments/{commentId}/reply/{replyCommentId}")
    public String updateReplyComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                     @PathVariable("replyCommentId") Long replyCommentId, @Valid @RequestBody ReplyCommentRequest request) {
        arduinoCommunityService.updateReplyComment(arduinoId, commentId, replyCommentId, request);
        return "Updated reply comment";
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}/reply/{replyCommentId}")
    public String deleteReplyComment(@PathVariable("arduinoId") Long arduinoId, @PathVariable("commentId") Long commentId,
                                     @PathVariable("replyCommentId") Long replyCommentId) {
        arduinoCommunityService.deleteReplyComment(arduinoId, commentId, replyCommentId);
        return "Deleted reply comment";
    }

    @PostMapping("/{arduinoId}/like")
    public String likeArduino(@PathVariable("arduinoId") Long arduinoId) {
        arduinoCommunityService.likeArduino(arduinoId);
        return "Liked arduino";
    }

    @DeleteMapping("/{arduinoId}/like")
    public String unlikeArduino(@PathVariable("arduinoId") Long arduinoId) {
        arduinoCommunityService.unlikeArduino(arduinoId);
        return "Unliked arduino";
    }
}
