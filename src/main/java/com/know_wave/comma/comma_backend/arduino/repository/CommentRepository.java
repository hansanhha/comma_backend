package com.know_wave.comma.comma_backend.arduino.repository;

import com.know_wave.comma.comma_backend.arduino.entity.Arduino;
import com.know_wave.comma.comma_backend.arduino.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Query("select c " +
            "from Comment c " +
            "join fetch c.account " +
            "where c.parent = :comment")
    List<Comment> findFetchReplyCommentByComment(Comment comment, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "join fetch c.account " +
            "where c.arduino = :arduino " +
            "and c.parent = null")
     List<Comment> findAllPagingByArduino(Arduino arduino, Pageable pageable);
}
