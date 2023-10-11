package com.know_wave.comma.comma_backend.arduino.dto.comment;

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
