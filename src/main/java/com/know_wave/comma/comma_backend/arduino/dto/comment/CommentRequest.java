package com.know_wave.comma.comma_backend.arduino.dto.comment;

import jakarta.validation.constraints.NotEmpty;

public class CommentRequest {

    @NotEmpty(message = "{Required}")
    private String arduinoUserComment;

    public String getArduinoUserComment() {
        return arduinoUserComment;
    }

    public void setArduinoUserComment(String arduinoUserComment) {
        this.arduinoUserComment = arduinoUserComment;
    }
}
