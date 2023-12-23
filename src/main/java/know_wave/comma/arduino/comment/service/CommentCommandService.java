package know_wave.comma.arduino.comment.service;

import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.system.AccountQueryService;
import know_wave.comma.common.entity.ExceptionMessageSource;
import know_wave.comma.arduino.comment.dto.*;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.entity.CommentLike;
import know_wave.comma.arduino.comment.repository.CommentLikeRepository;
import know_wave.comma.arduino.comment.repository.CommentRepository;
import know_wave.comma.account.aop.CheckAccountStatus;
import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.component.service.ComponentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@CheckAccountStatus
public class CommentCommandService {

    private final AccountQueryService accountQueryService;
    private final ComponentQueryService componentQueryService;

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    public void writeComment(Long arduinoId, CommentWriteRequest commentWriteForm) {
        Arduino arduino = componentQueryService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount();
        Comment comment = Comment.create(arduino, account, commentWriteForm.getContent());

        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, CommentUpdateRequest commentUpdateForm) {
        Account account = accountQueryService.findAccount();
        Comment comment = findComment(commentId);

        if (comment.isNotWriter(account)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_WRITER);
        }

        comment.update(commentUpdateForm.getUpdatedContent());
    }

    public void deleteComment(Long commentId) {
        Account account = accountQueryService.findAccount();
        Comment comment = findComment(commentId);

        if (comment.isNotWriter(account)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_WRITER);
        }

        comment.delete();
    }

    public void writeReplyComment(Long arduinoId, Long replyCommentId, ReplyCommentWriteRequest replyCommentWriteForm) {
        Comment parentComment = findComment(replyCommentId);
        Arduino arduino = componentQueryService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount();

        Comment replyComment = Comment.createReply(arduino, account, parentComment, replyCommentWriteForm.getReplyContent());

        commentRepository.save(replyComment);
    }

    public void updateReplyComment(Long replayCommentId, ReplyCommentUpdateRequest replyCommentUpdateForm) {
        Account account = accountQueryService.findAccount();
        Comment replyComment = findComment(replayCommentId);

        if (replyComment.isNotWriter(account)) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_WRITER);
        }

        replyComment.update(replyCommentUpdateForm.getUpdatedReplyContent());
    }

    public void deleteReplyComment(Long replyCommentId) {
        deleteComment(replyCommentId);
    }

    public void likeComment(Long commentId) {
        Account account = accountQueryService.findAccount();
        Comment comment = findComment(commentId);
        Optional<CommentLike> liked = commentLikeRepository.findByAccount(comment, account);

        if (liked.isEmpty()) {
            CommentLike like = CommentLike.create(account, comment);
            commentLikeRepository.save(like);
        }
    }

    public void unlikeComment(Long commentId) {
        Account account = accountQueryService.findAccount();
        Comment comment = findComment(commentId);
        Optional<CommentLike> liked = commentLikeRepository.findByAccount(comment, account);

        liked.ifPresent(commentLikeRepository::delete);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_COMMENT));
    }
}
