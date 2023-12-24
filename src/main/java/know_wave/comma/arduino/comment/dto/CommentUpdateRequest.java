package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CommentUpdateRequest {

    @JsonProperty("update_content")
    private String updatedContent;
}
