package know_wave.comma.arduino_.dto.comment;

import jakarta.validation.constraints.NotEmpty;

public class ReplyCommentRequest {

    @NotEmpty(message = "{Required}")
    private String arduinoUserReplyComment;

    public String getArduinoUserReplyComment() {
        return arduinoUserReplyComment;
    }

    public void setArduinoUserReplyComment(String arduinoUserReplyComment) {
        this.arduinoUserReplyComment = arduinoUserReplyComment;
    }
}
