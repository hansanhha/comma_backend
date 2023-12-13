package know_wave.comma.arduino.comment.repository;

import know_wave.comma.account.entity.Account;
import know_wave.comma.arduino.comment.entity.Comment;
import know_wave.comma.arduino.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArduinoCommentLikeRepository extends CrudRepository<CommentLike, Long> {

    @Query("select cl from CommentLike cl where cl.comment = :comment and cl.account = :account")
    Optional<CommentLike> findByAccount(Comment comment, Account account);
}
