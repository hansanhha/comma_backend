package com.know_wave.comma.comma_backend.order.dto;

import com.know_wave.comma.comma_backend.order.entity.Subject;
import com.know_wave.comma.comma_backend.payment.entity.PaymentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRequest {

    @NotNull(message = "{Required}")
    private Subject subject;

    @NotNull(message = "{Required}")
    private PaymentType paymentType;

    private String sseId;
}
