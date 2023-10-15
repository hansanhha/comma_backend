package com.know_wave.comma.comma_backend.arduino.service.normal;

import com.know_wave.comma.comma_backend.account.entity.Account;
import com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService;
import com.know_wave.comma.comma_backend.arduino.dto.arduino.ArduinoReplyCommentResponse;
import com.know_wave.comma.comma_backend.arduino.dto.comment.CommentRequest;
import com.know_wave.comma.comma_backend.arduino.dto.comment.ReplyCommentRequest;
import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Comment;
import com.know_wave.comma.comma_backend.arduino.entity.Like;
import com.know_wave.comma.comma_backend.arduino.repository.CommentRepository;
import com.know_wave.comma.comma_backend.arduino.repository.LikeRepository;
import com.know_wave.comma.comma_backend.util.ValidateUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.know_wave.comma.comma_backend.account.service.normal.AccountQueryService.getAuthenticatedId;
import static com.know_wave.comma.comma_backend.util.message.ExceptionMessageSource.*;

@Service
@Transactional
public class ArduinoCommunityService {

    private final AccountQueryService accountQueryService;
    private final ArduinoService arduinoService;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ArduinoCommunityService(AccountQueryService accountQueryService, ArduinoService arduinoService, CommentRepository commentRepository, LikeRepository likeRepository) {
        this.accountQueryService = accountQueryService;
        this.arduinoService = arduinoService;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    public void submitComment(Long arduinoId, CommentRequest request) {
        Arduino arduino = arduinoService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount(getAuthenticatedId());
        String commentContent = request.getArduinoUserComment();

        commentRepository.save(new Comment(arduino, account, commentContent));
    }

    public void updateComment(Long arduinoId, Long commentId, CommentRequest request) {
        Account account = accountQueryService.findAccount(getAuthenticatedId());
        String commentContent = request.getArduinoUserComment();

        Comment comment = getComment(commentId);

        checkIsOwnComment(comment, account);
        comment.setContent(commentContent);
    }

    public void deleteComment(Long arduinoId, Long commentId) {
        Account account = accountQueryService.findAccount(getAuthenticatedId());

        Comment comment = getComment(commentId);

        checkIsOwnComment(comment, account);
        commentRepository.delete(comment);
    }

    public List<ArduinoReplyCommentResponse> getReplyComment(Long arduinoId, Long commentId, Pageable pageable) {

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Comment comment = getComment(commentId);

        List<Comment> replyComments = getReplyComments(comment, pageRequest);

        return ArduinoReplyCommentResponse.of(replyComments);
    }

    public void submitReplyComment(Long arduinoId, Long commentId, ReplyCommentRequest request) {
        Arduino arduino = arduinoService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount(getAuthenticatedId());
        Comment comment = getComment(commentId);
        String replyCommentContent = request.getArduinoUserReplyComment();

        commentRepository.save(new Comment(arduino, account, comment, replyCommentContent));
    }

    public void updateReplyComment(Long arduinoId, Long commentId,
                                   Long replyCommentId, ReplyCommentRequest request) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());
        String replyCommentContent = request.getArduinoUserReplyComment();

        Comment replyComment = getComment(replyCommentId);

        checkIsOwnComment(replyComment, account);

        replyComment.setContent(replyCommentContent);
    }

    public void deleteReplyComment(Long arduinoId, Long commentId,
                                   Long replyCommentId) {

        Account account = accountQueryService.findAccount(getAuthenticatedId());


        Comment replyComment = getComment(replyCommentId);

        checkIsOwnComment(replyComment, account);

        commentRepository.delete(replyComment);
    }

    public void likeArduino(Long arduinoId) {
        Arduino arduino = arduinoService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount(getAuthenticatedId());

        likeRepository.findByArduinoAndAccount(arduino, account).ifPresentOrElse(
                        likeRepository::delete,
                        () -> likeRepository.save(new Like(account, arduino)));
    }

    public void unlikeArduino(Long arduinoId) {
        Arduino arduino = arduinoService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount(getAuthenticatedId());

        likeRepository.findByArduinoAndAccount(arduino, account).ifPresentOrElse(
                        likeRepository::delete,
                        () -> {throw new EntityNotFoundException(NOT_FOUND_VALUE);}
        );
    }

    public Comment getComment(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);

        ValidateUtils.throwIfEmpty(commentOptional);

        return commentOptional.get();
    }

    public List<Comment> getReplyComments(Comment comment, PageRequest pageRequest) {
        List<Comment> replyComments = commentRepository.findFetchReplyCommentByComment(comment, pageRequest);

        ValidateUtils.throwIfEmpty(replyComments);

        return replyComments;
    }

    private static void checkIsOwnComment(Comment comment, Account account) {
        if (comment.isNotWriter(account.getId())) {
            throw new BadCredentialsException(BAD_CREDENTIALS);
        }
    }
}
