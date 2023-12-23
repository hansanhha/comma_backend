package know_wave.comma.arduino.comment.repository;

import know_wave.comma.arduino.component.entity.Arduino;
import know_wave.comma.arduino.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.arduino = :arduino and c.parent is null")
    Page<Comment> findAllByArduino(Arduino arduino, Pageable pageable);

    @Query("select c from Comment c where c.parent = :parent")
    Page<Comment> findAllReplyById(Comment parent, Pageable pageable);
}
