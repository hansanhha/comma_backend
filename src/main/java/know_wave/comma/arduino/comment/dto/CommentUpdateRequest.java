package know_wave.comma.arduino.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class CommentUpdateRequest {

    public static CommentUpdateRequest create(String updatedContent) {
        return new CommentUpdateRequest(updatedContent);
    }

    @JsonProperty("update_content")
    private String updatedContent;
}
