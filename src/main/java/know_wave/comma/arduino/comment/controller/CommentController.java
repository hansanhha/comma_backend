package know_wave.comma.arduino.comment.controller;

import jakarta.validation.Valid;
import know_wave.comma.arduino.comment.dto.CommentUpdateRequest;
import know_wave.comma.arduino.comment.dto.CommentWriteRequest;
import know_wave.comma.arduino.comment.dto.ReplyCommentUpdateRequest;
import know_wave.comma.arduino.comment.dto.ReplyCommentWriteRequest;
import know_wave.comma.arduino.comment.service.CommentCommandService;
import know_wave.comma.arduino.comment.service.CommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/arduinos")
@RequiredArgsConstructor
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    private static final String MESSAGE = "message";
    private static final String DATA = "body";

    @GetMapping("/{arduinoId}/comments")
    public Map<String, Object> getComments(@PathVariable Long arduinoId, Pageable pageable) {
        return Map.of(MESSAGE, "comments", DATA, commentQueryService.getComments(arduinoId, pageable));
    }

    @GetMapping("/{arduinoId}/comments/{commentId}/reply")
    public Map<String, Object> getReplyComments(@PathVariable Long arduinoId,
                                                @PathVariable Long commentId,
                                                Pageable pageable) {
        return Map.of(MESSAGE, "reply comments", DATA, commentQueryService.getReplyComments(commentId, pageable));
    }


    @PostMapping("/{arduinoId}/comment")
    public Map<String, Object> writeComment(@PathVariable Long arduinoId,
                                            @Valid @RequestBody CommentWriteRequest writeRequest) {
        commentCommandService.writeComment(arduinoId, writeRequest);
        return Map.of(MESSAGE, "written comment");
    }

    @PatchMapping("/{arduinoId}/comments/{commentId}")
    public Map<String, Object> updateComment(@PathVariable Long arduinoId,
                                             @PathVariable Long commentId,
                                             @Valid @RequestBody CommentUpdateRequest updateRequest) {
        commentCommandService.updateComment(commentId, updateRequest);
        return Map.of(MESSAGE, "updated comment");
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}")
    public Map<String, Object> deleteComment(@PathVariable Long arduinoId,
                                             @PathVariable Long commentId) {
        commentCommandService.deleteComment(commentId);
        return Map.of(MESSAGE, "deleted comment");
    }

    @PostMapping("/{arduinoId}/comments/{commentId}/reply")
    public Map<String, Object> writeReply(@PathVariable Long arduinoId,
                                          @PathVariable Long commentId,
                                          @Valid @RequestBody ReplyCommentWriteRequest writeRequest) {
        commentCommandService.writeReplyComment(arduinoId, commentId, writeRequest);
        return Map.of(MESSAGE, "written reply");
    }

    @PatchMapping("/{arduinoId}/comments/{commentId}/reply/{replyCommentId}")
    public Map<String, Object> updateReply(@PathVariable Long arduinoId,
                                           @PathVariable Long commentId,
                                           @PathVariable Long replyCommentId,
                                           @Valid @RequestBody ReplyCommentUpdateRequest updateRequest) {
        commentCommandService.updateReplyComment(replyCommentId, updateRequest);
        return Map.of(MESSAGE, "updated reply");
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}/reply/{replyCommentId}")
    public Map<String, Object> deleteReply(@PathVariable Long arduinoId,
                                           @PathVariable Long commentId,
                                           @PathVariable Long replyCommentId) {
        commentCommandService.deleteReplyComment(replyCommentId);
        return Map.of(MESSAGE, "deleted reply");
    }

    @PostMapping("/{arduinoId}/comments/{commentId}/like")
    public Map<String, Object> likeComment(@PathVariable Long arduinoId,
                                           @PathVariable Long commentId) {
        commentCommandService.likeComment(commentId);
        return Map.of(MESSAGE, "liked comment");
    }

    @DeleteMapping("/{arduinoId}/comments/{commentId}/like")
    public Map<String, Object> unlikeComment(@PathVariable Long arduinoId,
                                             @PathVariable Long commentId) {
        commentCommandService.unlikeComment(commentId);
        return Map.of(MESSAGE, "unliked comment");
    }

}
