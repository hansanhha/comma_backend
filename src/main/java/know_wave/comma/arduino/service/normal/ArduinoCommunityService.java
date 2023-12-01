package know_wave.comma.arduino.service.normal;

import jakarta.persistence.EntityNotFoundException;
import know_wave.comma.account.entity.Account;
import know_wave.comma.account.service.normal.AccountQueryService;
import know_wave.comma.arduino.dto.arduino.ArduinoReplyCommentResponse;
import know_wave.comma.arduino.dto.comment.CommentRequest;
import know_wave.comma.arduino.dto.comment.ReplyCommentRequest;
import know_wave.comma.arduino.entity.Arduino;
import know_wave.comma.arduino.entity.Comment;
import know_wave.comma.arduino.entity.Like;
import know_wave.comma.arduino.repository.CommentRepository;
import know_wave.comma.arduino.repository.LikeRepository;
import know_wave.comma.alarm.util.ExceptionMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        String commentContent = request.getArduinoUserComment();

        commentRepository.save(new Comment(arduino, account, commentContent));
    }

    public void updateComment(Long arduinoId, Long commentId, CommentRequest request) {
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        String commentContent = request.getArduinoUserComment();

        Comment comment = getComment(commentId);

        checkIsOwnComment(comment, account);
        comment.setContent(commentContent);
    }

    public void deleteComment(Long arduinoId, Long commentId) {
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());

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
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        Comment comment = getComment(commentId);
        String replyCommentContent = request.getArduinoUserReplyComment();

        commentRepository.save(new Comment(arduino, account, comment, replyCommentContent));
    }

    public void updateReplyComment(Long arduinoId, Long commentId,
                                   Long replyCommentId, ReplyCommentRequest request) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());
        String replyCommentContent = request.getArduinoUserReplyComment();

        Comment replyComment = getComment(replyCommentId);

        checkIsOwnComment(replyComment, account);

        replyComment.setContent(replyCommentContent);
    }

    public void deleteReplyComment(Long arduinoId, Long commentId,
                                   Long replyCommentId) {

        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());


        Comment replyComment = getComment(replyCommentId);

        checkIsOwnComment(replyComment, account);

        commentRepository.delete(replyComment);
    }

    public void likeArduino(Long arduinoId) {
        Arduino arduino = arduinoService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());

        likeRepository.findByArduinoAndAccount(arduino, account).ifPresentOrElse(
                        likeRepository::delete,
                        () -> likeRepository.save(new Like(account, arduino)));
    }

    public void unlikeArduino(Long arduinoId) {
        Arduino arduino = arduinoService.getArduino(arduinoId);
        Account account = accountQueryService.findAccount(accountQueryService.getAuthenticatedId());

        likeRepository.findByArduinoAndAccount(arduino, account).ifPresentOrElse(
                        likeRepository::delete,
                        () -> {throw new EntityNotFoundException(ExceptionMessageSource.NOT_FOUND_VALUE);}
        );
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> getReplyComments(Comment comment, PageRequest pageRequest) {
        List<Comment> replyComments = commentRepository.findFetchReplyCommentByComment(comment, pageRequest);

        throwIfEmpty(replyComments);

        return replyComments;
    }

    private static void checkIsOwnComment(Comment comment, Account account) {
        if (comment.isNotWriter(account.getId())) {
            throw new BadCredentialsException(ExceptionMessageSource.BAD_CREDENTIALS);
        }
    }

    private void throwIfEmpty(Iterable<?> iterable) {
        if (!iterable.iterator().hasNext()) {
            throw new IllegalArgumentException(ExceptionMessageSource.NOT_FOUND_VALUE);
        }
    }
}
