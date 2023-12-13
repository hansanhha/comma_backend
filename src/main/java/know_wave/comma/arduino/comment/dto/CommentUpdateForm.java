package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentUpdateForm {

    @JsonProperty("target_comment_id")
    private Long commentId;

    @JsonProperty("updated_content")
    private String updatedContent;
}
