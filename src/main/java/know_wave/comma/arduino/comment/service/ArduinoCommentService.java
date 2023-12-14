package know_wave.comma.arduino.comment.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.arduino.comment.dto.*;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.entity.CommentLike;
import know_wave.comma.arduino.comment.repository.ArduinoCommentLikeRepository;
import know_wave.comma.arduino.comment.repository.ArduinoCommentRepository;
import know_wave.comma.account.config.CheckAccountStatus;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.service.ArduinoComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@CheckAccountStatus
public class ArduinoCommentService {

    private final AccountQueryService accountQueryService;
    private final ArduinoComponentService arduinoInfoService;
    private final ArduinoCommentRepository commentRepository;
    private final ArduinoCommentLikeRepository commentLikeRepository;

    public void writeComment(CommentWriteForm commentWriteForm) {
        Arduino arduino = arduinoInfoService.getArduino(commentWriteForm.getArduinoId());
        Account account = accountQueryService.findAccount();
        Comment comment = Comment.create(arduino, account, commentWriteForm.getContent());

        commentRepository.save(comment);
    }

    public void updateComment(CommentUpdateForm commentUpdateForm) {
        Account account = accountQueryService.findAccount();
        Comment comment = getComment(commentUpdateForm.getCommentId());

        if (comment.isNotWriter(account)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_WRITER);
        }

        comment.update(commentUpdateForm.getUpdatedContent());
    }

    public void deleteComment(Long commentId) {
        Account account = accountQueryService.findAccount();
        Comment comment = getComment(commentId);

        if (comment.isNotWriter(account)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_WRITER);
        }

        comment.delete();
    }

    public void writeReplyComment(ReplyCommentWriteForm replyCommentWriteForm) {
        Comment parentComment = getComment(replyCommentWriteForm.getCommentId());
        Arduino arduino = arduinoInfoService.getArduino(replyCommentWriteForm.getArduinoId());
        Account account = accountQueryService.findAccount();

        Comment replyComment = Comment.createReply(arduino, account, parentComment, replyCommentWriteForm.getReplyContent());

        commentRepository.save(replyComment);
    }

    public void updateReplyComment(ReplyCommentUpdateForm replyCommentUpdateForm) {
        Account account = accountQueryService.findAccount();
        Comment replyComment = getComment(replyCommentUpdateForm.getReplyCommentId());

        if (replyComment.isNotWriter(account)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_WRITER);
        }

        replyComment.update(replyCommentUpdateForm.getUpdatedReplyContent());
    }

    public void deleteReplyComment(Long replyCommentId) {
        deleteComment(replyCommentId);
    }

    public void likeComment(CommentLikeRequest request) {
        Account account = accountQueryService.findAccount();
        Comment comment = getComment(request.getCommentId());
        Optional<CommentLike> liked = commentLikeRepository.findByAccount(comment, account);

        if (liked.isEmpty()) {
            CommentLike like = CommentLike.create(account, comment);
            commentLikeRepository.save(like);
        }
    }

    public void unlikeComment(CommentLikeRequest request) {
        Account account = accountQueryService.findAccount();
        Comment comment = getComment(request.getCommentId());
        Optional<CommentLike> liked = commentLikeRepository.findByAccount(comment, account);

        if (liked.isPresent()) {
            commentLikeRepository.delete(liked.get());
        }
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_EXIST_COMMENT));
    }

}
