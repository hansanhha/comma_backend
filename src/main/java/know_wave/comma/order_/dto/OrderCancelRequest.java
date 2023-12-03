package know_wave.comma.order_.dto;

import jakarta.validation.constraints.NotEmpty;

public class OrderCancelRequest {

    @NotEmpty(message = "{Required}")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
