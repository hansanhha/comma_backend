package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentLikeRequest {

    @JsonProperty("target_comment_id")
    private final Long commentId;

}
